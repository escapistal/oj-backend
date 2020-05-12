package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.ContestSubmissionRepository;
import com.xc.oj.repository.SubmissionBaseRepository;
import com.xc.oj.repository.SubmissionRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import com.xc.oj.util.OJPropertiesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

@Service
public class CommonService {

    @Value("${file.uploadDir}")
    private String uploadDir;

    final SubmissionBaseRepository submissionBaseService;
    final SubmissionRepository submissionService;
    final ContestSubmissionRepository contestSubmissionService;
    final AcmContestRankService acmContestRankService;
    final UserService userService;
    final ProblemBaseService problemBaseService;
    final ProblemService problemService;
    final ContestProblemService contestProblemService;
    private final RedisTemplate<String,Object> redisTemplate;

    public CommonService(SubmissionBaseRepository submissionBaseService, SubmissionRepository submissionService, ContestSubmissionRepository contestSubmissionService, AcmContestRankService acmContestRankService, UserService userService, ProblemBaseService problemBaseService, ProblemService problemService, ContestProblemService contestProblemService, RedisTemplate<String, Object> redisTemplate) {
        this.submissionBaseService = submissionBaseService;
        this.submissionService = submissionService;
        this.contestSubmissionService = contestSubmissionService;
        this.acmContestRankService = acmContestRankService;
        this.userService = userService;
        this.problemBaseService = problemBaseService;
        this.problemService = problemService;
        this.contestProblemService = contestProblemService;
        this.redisTemplate = redisTemplate;
    }

    public responseBase<String> submit(SubmissionBase submission){

        submission.setStatus(JudgeResultEnum.PENDING);
        submission.setCodeLength(submission.getCode().length());
//        submission.setDetail(new ArrayList<>());
        submission.setUser(new UserInfo(AuthUtil.getId()));
        submission.setCreateTime(new Timestamp(new Date().getTime()));
//        submission.setExecuteTime(0);
//        submission.setExecuteMemory(0);
        submissionBaseService.save(submission);
        ProblemBase problem=problemBaseService.findById(submission.getProblem().getId()).orElse(null);

        JudgeTask judgeTask=new JudgeTask();
        judgeTask.setLazyEval(Boolean.parseBoolean(OJPropertiesUtil.get("lazy-eval")));
        judgeTask.setSubmissionId(submission.getId());
        judgeTask.setLanguage(submission.getLanguage());
        judgeTask.setCode(submission.getCode());
        judgeTask.setTimeLimit(problem.getRealTimeLimit(submission.getLanguage()));
        judgeTask.setMemoryLimit(problem.getRealMemoryLimit(submission.getLanguage()));
        judgeTask.setTestcaseMd5(problem.getTestcaseMd5());
        System.out.println(judgeTask.getTimeLimit());
        System.out.println(judgeTask.getMemoryLimit());
        if(problem.getSpj())
            judgeTask.setSpjMd5(problem.getSpjMd5());
        redisTemplate.opsForList().leftPush("JudgeTask",judgeTask);
        return responseBuilder.success();
    }

    private void getSummary(SubmissionBase submission,JudgeResult judgeResult){//补充评测细节并存库
        Integer maxExecTime=0;
        Integer maxExecMemory=0;
        if(judgeResult.getResult()!=JudgeResultEnum.CE&&judgeResult.getResult()!=JudgeResultEnum.SE){
            JudgeResultEnum firstErr = null;
            int err = 0, pe = 0, se = 0;
            for (SingleJudgeResult res : judgeResult.getDetail()) {
                maxExecTime = Math.max(maxExecTime, res.getExecTime());
                maxExecMemory = Math.max(maxExecMemory, res.getExecMemory());
                if (res.getResult() != JudgeResultEnum.AC) {
                    ++err;
                    if (res.getResult() == JudgeResultEnum.PE)
                        ++pe;
                    if (res.getResult() == JudgeResultEnum.SE)
                        ++se;
                    if (firstErr == null && res.getResult() != JudgeResultEnum.PE)
                        firstErr = res.getResult();
                }
            }
            if (se > 0)
                submission.setStatus(JudgeResultEnum.SE);
            else if (err == 0)//AC
                submission.setStatus(JudgeResultEnum.AC);
            else if (err == pe || firstErr == null)//全部非AC点都PE才算PE
                submission.setStatus(JudgeResultEnum.PE);
            else
                submission.setStatus(firstErr);
        }
        else
            submission.setStatus(judgeResult.getResult());
        submission.setDetail(judgeResult.getDetail());
        submission.setExecuteTime(maxExecTime);
        submission.setExecuteMemory(maxExecMemory);
        submission.setJudgeTime(new Timestamp(new Date().getTime()));
        submissionBaseService.save(submission);
    }

    public void updateSubmissionResult(JudgeResult judgeResult) {
        Long sid = judgeResult.getSubmissionId();
        SubmissionBase submissionBase = submissionBaseService.findById(sid).orElse(null);
        getSummary(submissionBase, judgeResult);
        if (submissionBase instanceof ContestSubmission) {//赛题提交分流
            updateContestSubmissionResult((ContestSubmission) submissionBase,judgeResult);
            return;
        }
        //常规题库提交
        Submission submission = (Submission) submissionBase;
        Problem problem = (Problem) submission.getProblem();
        User user = userService.findById(submission.getUser().getId()).orElse(null);
        boolean upd = false;
        if (submission.getStatus() == JudgeResultEnum.AC) {//AC结果
            if (!user.getAcceptedId().contains(problem.getId())) {//未曾AC过
                problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                problem.setAcceptedNumber(problem.getAcceptedNumber() + 1);
                user.setSubmissionNumber(user.getSubmissionNumber() + 1);
                user.setAcceptedNumber(user.getAcceptedId().size());
                user.getAcceptedId().add(problem.getId());
                upd = true;
            }
        } else if (!Arrays.asList(JudgeResultEnum.SE, JudgeResultEnum.CE).contains(submission.getStatus())) {//其他除SE与CE结果
            problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
            user.setSubmissionNumber(user.getSubmissionNumber() + 1);
            upd = true;
        }
        if (upd) {
            userService.save(user);
            problemService.save(problem);
        }
    }

    private void updateContestSubmissionResult(ContestSubmission submission,JudgeResult judgeResult) {
        //TODO 评测结果入redis缓存
        ContestInfo contest=submission.getContest();
        ContestProblem problem= (ContestProblem) submission.getProblem();
        AcmContestRank acmContestRank,acmContestRankLocked;
        SingleSubmissionInfo info;
        //赛中且非管理员提交，才需要更新
        if(!submission.getCreateTime().before(contest.getStartTime())&&!submission.getCreateTime().after(contest.getEndTime())
                && !submission.getUser().getRole().contains("admin")) {
            acmContestRank = acmContestRankService.findByContestIdAndUserIdAndLocked(contest.getId(), submission.getUser().getId(), false).orElse(null);
            acmContestRankLocked = acmContestRankService.findByContestIdAndUserIdAndLocked(contest.getId(), submission.getUser().getId(), true).orElse(null);
            if (acmContestRank == null) {
                acmContestRank = new AcmContestRank();
                acmContestRank.setLocked(false);
                acmContestRank.setContestId(contest.getId());
                acmContestRank.setUser(submission.getUser());
            }
            if (acmContestRankLocked == null) {
                acmContestRankLocked = new AcmContestRank();
                acmContestRankLocked.setLocked(true);
                acmContestRankLocked.setContestId(contest.getId());
                acmContestRankLocked.setUser(submission.getUser());
            }
            if (acmContestRank.getSubmissionInfo().get(problem.getId()) == null
                    || !acmContestRank.getSubmissionInfo().get(problem.getId()).getAc()) {//此前未ac过才需要更新榜单
                if (submission.getStatus() == JudgeResultEnum.AC) {//AC结果
                    problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                    problem.setAcceptedNumber(problem.getAcceptedNumber() + 1);
                    acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber() + 1);
                    acmContestRank.setAcceptedNumber(acmContestRank.getAcceptedNumber() + 1);
                    info = acmContestRank.getSubmissionInfo().get(problem.getId());
                    if (info == null)
                        info = new SingleSubmissionInfo();
                    info.setAc(true);
                    info.setAcTime((int) ((submission.getCreateTime().getTime() - contest.getStartTime().getTime()) / 1000));
                    //优先遵从contest局部配置罚时，否则遵从配置文件
                    Integer penaltyTime=contest.getPenaltyTime()==null? Integer.valueOf(OJPropertiesUtil.get("penalty_time")) :contest.getPenaltyTime();
                    acmContestRank.setTotalTime(acmContestRank.getTotalTime() + info.getError() * penaltyTime + info.getAcTime());
                    acmContestRank.getSubmissionInfo().put(problem.getId(), info);
                    if (!contest.getWillLock() || submission.getCreateTime().before(contest.getLockTime())) {//不封榜或未到封榜时间
                        problem.setSubmissionNumberLocked(problem.getSubmissionNumberLocked() + 1);
                        problem.setAcceptedNumberLocked(problem.getAcceptedNumberLocked() + 1);
                        acmContestRankLocked.setSubmissionNumber(acmContestRankLocked.getSubmissionNumber() + 1);
                        acmContestRankLocked.setAcceptedNumber(acmContestRankLocked.getAcceptedNumber() + 1);
                        acmContestRankLocked.setTotalTime(acmContestRank.getTotalTime());
                        acmContestRankLocked.getSubmissionInfo().put(problem.getId(), info);
                    }
                } else {//非AC结果
                    problem.setSubmissionNumber(problem.getSubmissionNumber() + 1);
                    acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber() + 1);
                    info = acmContestRank.getSubmissionInfo().get(problem.getId());
                    if (info == null)
                        info = new SingleSubmissionInfo();
                    info.setError(info.getError() + 1);
                    acmContestRank.getSubmissionInfo().put(problem.getId(), info);
                    if (!contest.getWillLock() || submission.getCreateTime().before(contest.getLockTime())) {//不封榜或未到封榜时间
                        problem.setSubmissionNumberLocked(problem.getSubmissionNumberLocked() + 1);
                        acmContestRankLocked.setSubmissionNumber(acmContestRankLocked.getSubmissionNumber() + 1);
                        acmContestRankLocked.getSubmissionInfo().put(problem.getId(), info);
                    }
                }
            }
            contestProblemService.save(problem);
            acmContestRankService.save(acmContestRank);
            acmContestRankService.save(acmContestRankLocked);
        }
    }

    public static void flow(InputStream in, OutputStream out) throws IOException {
        int bytesRead;
        byte[] buffer = new byte[8192];
        while ((bytesRead = in.read(buffer, 0, 8192)) != -1)
            out.write(buffer, 0, bytesRead);
    }

    public responseBase upload(MultipartFile file) {
        String md5;
        String ext;
        try {
            InputStream is = file.getInputStream();
            OutputStream os;
            md5=DigestUtils.md5DigestAsHex(is);
            is.close();
            String fileName=file.getOriginalFilename();
            ext=fileName.substring(fileName.lastIndexOf("."));
            File dir = new File(uploadDir + md5 + ext);
            if(dir.exists())
                dir.delete();
            dir.createNewFile();
            is=file.getInputStream();
            os=new FileOutputStream(dir);
            flow(is,os);
            is.close();
            os.close();
        }catch(Exception e){
            e.printStackTrace();
            return responseBuilder.fail(responseCode.READ_FILE_ERROR);
        }
        return responseBuilder.success("api/img/" + md5 + ext);
    }

    public responseBase changeOption(String key, String value) {
        OJPropertiesUtil.store(key,value);
        return responseBuilder.success();
    }

    public responseBase<Properties> getOptions() {
        System.out.println(OJPropertiesUtil.getProperties().stringPropertyNames());
        return responseBuilder.success(OJPropertiesUtil.getProperties());
    }
}
