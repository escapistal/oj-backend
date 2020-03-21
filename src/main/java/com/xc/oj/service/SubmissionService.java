package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.*;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import com.xc.oj.util.OJPropertiesUtil;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
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

    public responseBase<Page<Submission>> list(Long cid, Long pid, Long uid, String uname,int page,int size) {
        Page<Submission> submissions;
//        if(cid!=null&&cid>0) {//TODO 比赛status，cid筛选预计废弃，涉及contest的逻辑另外做，不支持筛选
//            Contest contest=contestService.findById(cid).orElse(null);
//            if(contest==null)
//                return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
//            Timestamp now=new Timestamp(new Date().getTime());
//            if(!AuthUtil.has("admin")&&!now.after(contest.getEndTime()))
//                return responseBuilder.fail(responseCode.FORBIDDEN);
//        }
        Submission submission=new Submission();
        if(pid!=null)
            submission.getProblem().setId(pid);
        if(uid!=null)//uid优先
            submission.setUser(new UserInfo(uid));
        else if(uname!=null&&!"".equals(uname.trim())) {
            UserInfo userInfo=new UserInfo();
            userInfo.setNickname(uname);
            submission.setUser(userInfo);
        }
        ExampleMatcher exampleMatcher=ExampleMatcher.matching()
                .withMatcher("user.nickname" ,ExampleMatcher.GenericPropertyMatchers.contains());
        Example<Submission> submissionExample = Example.of(submission ,exampleMatcher);
        PageRequest pageRequest=PageRequest.of(page,size,Sort.by(Sort.Order.desc("createTime")));
        submissions = submissionRepository.findAll(submissionExample,pageRequest);
        //TODO 懒加载
        submissions.forEach(s -> {
            s.setDetail(null);
            s.setCode(null);
        });

        return responseBuilder.success(submissions);
    }

    public responseBase<Submission> get(Long id) {
        Submission submission=submissionRepository.findById(id).orElse(null);
        if(submission==null)
            return responseBuilder.fail(responseCode.SUBMISSION_NOT_EXIST);
        return responseBuilder.success(submission);
    }

    public responseBase<String> submit(Submission submission){
        submission.setStatus(JudgeResultEnum.PENDING);
        submission.setCodeLength(submission.getCode().length());
        submission.setDetail(new ArrayList<>());
        submission.setCreateTime(new Timestamp(new Date().getTime()));
        submission.setExecuteTime(0);
        submission.setExecuteMemory(0);
        submissionRepository.save(submission);

//        Long cid=submission.getContestId();
//        Long pid=submission.getProblemId();
//        ContestProblem contestProblem;
//        Problem problem;
//        Integer timeLimit;
//        Integer memoryLimit;
//        List<HashMap<String,String>> allowLanguage;
//        if(cid!=0) {
//            contestProblem=contestProblemService.findById(pid).orElse(null);
//            if(contestProblem==null)
//                return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
//            problem=contestProblem.getProblem();
//            timeLimit=contestProblem.getTimeLimit();
//            memoryLimit=contestProblem.getMemoryLimit();
//            allowLanguage=contestProblem.getAllowLanguage();
//            if(timeLimit==null||timeLimit==0)
//                timeLimit=problem.getTimeLimit();
//            if(memoryLimit==null||memoryLimit==0)
//                memoryLimit=problem.getMemoryLimit();
//            if(allowLanguage==null||allowLanguage.isEmpty())
//                allowLanguage=problem.getAllowLanguage();
//        }
//        else {
//            problem = problemService.findById(pid).orElse(null);
//            if(problem==null)
//                return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
//            timeLimit=problem.getTimeLimit();
//            memoryLimit=problem.getMemoryLimit();
//            allowLanguage=problem.getAllowLanguage();
//        }
//        for(HashMap<String,String> mp:allowLanguage) {
//            if(mp.get("language").equals(submission.getLanguage())) {
//                timeLimit = (int)Math.round(timeLimit*Double.parseDouble(mp.get("factor")));
//                break;
//            }
//        }
        Problem problem=problemService.findById(submission.getProblem().getId()).orElse(null);
        JudgeTask judgeTask=new JudgeTask();
        judgeTask.setLazyEval(Boolean.parseBoolean(OJPropertiesUtil.get("lazy-eval")));
        judgeTask.setSubmissionId(submission.getId());
        judgeTask.setLanguage(submission.getLanguage());
        judgeTask.setCode(submission.getCode());
        judgeTask.setTimeLimit(problem.getRealTimeLimit(submission.getLanguage()));
        judgeTask.setMemoryLimit(problem.getRealMemoryLimit(submission.getLanguage()));
        judgeTask.setTestcaseMd5(problem.getTestCaseMd5());
        System.out.println(judgeTask.getTimeLimit());
        System.out.println(judgeTask.getMemoryLimit());
//        if(problem.getSpj())
//            judgeTask.setSpjMd5(problem.getSpjMd5());
        redisTemplate.opsForList().leftPush("JudgeTask",judgeTask);
        return responseBuilder.success();
    }



}


