package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.*;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ContestService contestService;
    private final ContestProblemService contestProblemService;
    private final ProblemService problemService;
    private final UserService userService;
    private final AcmContestRankService acmContestRankService;
    private final RedisTemplate<String,Object> redisTemplate;

    public SubmissionService(SubmissionRepository submissionRepository, ContestService contestService, ContestProblemService contestProblemService, ProblemService problemService, UserService userService, AcmContestRankService acmContestRankService, RedisTemplate<String, Object> redisTemplate) {
        this.submissionRepository = submissionRepository;
        this.contestService = contestService;
        this.contestProblemService = contestProblemService;
        this.problemService = problemService;
        this.userService = userService;
        this.acmContestRankService = acmContestRankService;
        this.redisTemplate = redisTemplate;
    }


    public Optional<Submission> findById(Long id){
        return submissionRepository.findById(id);
    }

    public void save(Submission submission){
        submissionRepository.save(submission);
    }

    public responseBase<String> submit(Submission submission){
        submission.setStatus(JudgeResultEnum.PENDING);
        submission.setCodeLength(submission.getCode().length());
        submission.setDetail(new ArrayList<>());
        submission.setCreateTime(new Timestamp(new Date().getTime()));
        submission.setExecuteTime(0);
        submission.setExecuteMemory(0);
        if(submission.getContestId()==null)
            submission.setContestId(0L);
        submissionRepository.save(submission);
        Long cid=submission.getContestId();
        Long pid=submission.getProblemId();
        ContestProblem contestProblem;
        Problem problem;
        Integer timeLimit;
        Integer memoryLimit;
        List<HashMap<String,String>> allowLanguage;
        if(cid!=0) {
            contestProblem=contestProblemService.findById(pid).orElse(null);
            if(contestProblem==null)
                return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
            problem=contestProblem.getProblem();
            timeLimit=contestProblem.getTimeLimit();
            memoryLimit=contestProblem.getMemoryLimit();
            allowLanguage=contestProblem.getAllowLanguage();
            if(timeLimit==null||timeLimit==0)
                timeLimit=problem.getTimeLimit();
            if(memoryLimit==null||memoryLimit==0)
                memoryLimit=problem.getMemoryLimit();
            if(allowLanguage==null||allowLanguage.isEmpty())
                allowLanguage=problem.getAllowLanguage();
        }
        else {
            problem = problemService.findById(pid).orElse(null);
            if(problem==null)
                return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
            timeLimit=problem.getTimeLimit();
            memoryLimit=problem.getMemoryLimit();
            allowLanguage=problem.getAllowLanguage();
        }
        for(HashMap<String,String> mp:allowLanguage) {
            if(mp.get("language").equals(submission.getLanguage())) {
                timeLimit = (int)Math.round(timeLimit*Double.parseDouble(mp.get("factor")));
                break;
            }
        }
        JudgeTask judgeTask=new JudgeTask();
        //TODO LazyEval应在config读取
        judgeTask.setLazyEval(false);
        judgeTask.setSubmissionId(submission.getId());
        judgeTask.setLanguage(submission.getLanguage());
        judgeTask.setCode(submission.getCode());
        judgeTask.setTimeLimit(timeLimit);
        judgeTask.setMemoryLimit(memoryLimit);
        judgeTask.setTestcaseMd5(problem.getTestCaseMd5());
        if(problem.getSpj())
            judgeTask.setSpjMd5(problem.getSpjMd5());
        redisTemplate.opsForList().leftPush("JudgeTask",judgeTask);
        return responseBuilder.success();
    }

    public responseBase<List<Submission>> listAll() {
        return responseBuilder.success(submissionRepository.findAll());
    }

    public void updateResult(JudgeResult judgeResult) {
        Long sid=judgeResult.getSubmissionId();
        Submission submission=submissionRepository.findById(sid).orElse(null);
        //TODO 评测结果入redis缓存
        Integer maxExecTime=0;
        Integer maxExecMemory=0;
        JudgeResultEnum firstErr=null;
        int err=0,pe=0;
        for (SingleJudgeResult res : judgeResult.getDetail()) {
            maxExecTime=Math.max(maxExecTime,res.getExecTime());
            maxExecMemory=Math.max(maxExecMemory,res.getExecMemory());
            if(res.getResult()!=JudgeResultEnum.AC){
                ++err;
                if(res.getResult()==JudgeResultEnum.PE)
                    ++pe;
                if(firstErr==null&&res.getResult()!=JudgeResultEnum.PE)
                    firstErr=res.getResult();
            }
        }
        if(err==0)//AC
            submission.setStatus(JudgeResultEnum.AC);
        else if(err==pe)//PE
            submission.setStatus(JudgeResultEnum.PE);
        else
            submission.setStatus(firstErr);
        submission.setDetail(judgeResult.getDetail());
        submission.setExecuteTime(maxExecTime);
        submission.setExecuteMemory(maxExecMemory);
        submission.setJudgeTime(new Timestamp(new Date().getTime()));
        submissionRepository.save(submission);

        Long cid=submission.getContestId();
        Long pid=submission.getProblemId();
        Contest contest;
        ContestProblem contestProblem;
        Problem problem;
        AcmContestRank acmContestRank,acmContestRankLocked;
        SingleSubmissionInfo info;
        if(cid!=0) {//赛题
            contest = contestService.findById(cid).orElse(null);
            contestProblem = contestProblemService.findById(pid).orElse(null);
            acmContestRank = acmContestRankService.findByContestIdAndUserIdAndLocked(cid, submission.getUser().getId(), false).orElse(null);
            acmContestRankLocked = acmContestRankService.findByContestIdAndUserIdAndLocked(cid, submission.getUser().getId(), true).orElse(null);
            if(acmContestRank==null) {
                acmContestRank = new AcmContestRank();
                acmContestRank.setLocked(false);
                acmContestRank.setContestId(cid);
                acmContestRank.setUser(submission.getUser());
            }
            if(acmContestRankLocked==null) {
                acmContestRankLocked = new AcmContestRank();
                acmContestRankLocked.setLocked(true);
                acmContestRankLocked.setContestId(cid);
                acmContestRankLocked.setUser(submission.getUser());
            }
            if (acmContestRank.getSubmissionInfo().get(pid) == null || !acmContestRank.getSubmissionInfo().get(pid).getAc()) {//此前未ac过
                if (submission.getStatus() == JudgeResultEnum.AC) {//AC结果
                    contestProblem.setSubmissionNumber(contestProblem.getSubmissionNumber() + 1);
                    contestProblem.setAcceptedNumber(contestProblem.getAcceptedNumber() + 1);
                    acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber()+1);
                    acmContestRank.setAcceptedNumber(acmContestRank.getAcceptedNumber() + 1);
                    info=acmContestRank.getSubmissionInfo().get(pid);
                    if(info==null)
                        info=new SingleSubmissionInfo();
                    info.setAc(true);
                    info.setAcTime((int)((submission.getCreateTime().getTime()-contest.getStartTime().getTime())/1000));
                    acmContestRank.setTotalTime(acmContestRank.getTotalTime()+info.getError()*20*60+info.getAcTime());//TODO 全局配置罚时时间
                    acmContestRank.getSubmissionInfo().put(pid,info);
                    if (!contest.getWillLock() || submission.getCreateTime().before(contest.getLockTime())) {//不封榜或未到封榜时间
                        contestProblem.setSubmissionNumberLocked(contestProblem.getSubmissionNumberLocked() + 1);
                        contestProblem.setAcceptedNumberLocked(contestProblem.getAcceptedNumberLocked() + 1);
                        acmContestRankLocked.setSubmissionNumber(acmContestRankLocked.getSubmissionNumber()+1);
                        acmContestRankLocked.setAcceptedNumber(acmContestRankLocked.getAcceptedNumber() + 1);
                        info=acmContestRankLocked.getSubmissionInfo().get(pid);
                        if(info==null)
                            info=new SingleSubmissionInfo();
                        info.setAc(true);
                        info.setAcTime((int)((submission.getCreateTime().getTime()-contest.getStartTime().getTime())/1000));
                        acmContestRankLocked.setTotalTime(acmContestRankLocked.getTotalTime()+info.getError()*20*60+info.getAcTime());//TODO 全局配置罚时时间
                        acmContestRankLocked.getSubmissionInfo().put(pid,info);
                    }
                } else {//非AC结果
                    contestProblem.setSubmissionNumber(contestProblem.getSubmissionNumber() + 1);
                    acmContestRank.setSubmissionNumber(acmContestRank.getSubmissionNumber()+1);
                    info=acmContestRank.getSubmissionInfo().get(pid);
                    if(info==null)
                        info=new SingleSubmissionInfo();
                    info.setError(info.getError()+1);
                    acmContestRank.getSubmissionInfo().put(pid,info);
                    if (!contest.getWillLock() || submission.getCreateTime().before(contest.getLockTime())) {//不封榜或未到封榜时间
                        contestProblem.setSubmissionNumberLocked(contestProblem.getSubmissionNumberLocked() + 1);
                        acmContestRankLocked.setSubmissionNumber(acmContestRankLocked.getSubmissionNumber()+1);
                        info=acmContestRankLocked.getSubmissionInfo().get(pid);
                        if(info==null)
                            info=new SingleSubmissionInfo();
                        info.setError(info.getError()+1);
                        acmContestRankLocked.getSubmissionInfo().put(pid,info);
                    }
                }
            }
            contestProblemService.save(contestProblem);
            acmContestRankService.save(acmContestRank);
            acmContestRankService.save(acmContestRankLocked);
        }
        else {//题库提交
            problem = problemService.findById(pid).orElse(null);
            User user=userService.findById(submission.getUser().getId()).orElse(null);
            problem.setSubmissionNumber(problem.getSubmissionNumber()+1);
            user.setSubmissionNumber(user.getSubmissionNumber()+1);
            if(submission.getStatus()==JudgeResultEnum.AC){
                if(!user.getAcceptedId().contains(pid)){//此前未AC过
                    problem.setAcceptedNumber(problem.getAcceptedNumber()+1);
                    user.getAcceptedId().add(pid);
                    user.setAcceptedNumber(user.getAcceptedId().size());
                }
            }
            userService.save(user);
            problemService.save(problem);
        }
    }
}


