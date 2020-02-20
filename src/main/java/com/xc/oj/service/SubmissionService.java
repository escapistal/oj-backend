package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.ContestProblemRepository;
import com.xc.oj.repository.ProblemRepository;
import com.xc.oj.repository.SubmissionRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;

    private final ProblemRepository problemRepository;

    private final ContestProblemRepository contestProblemRepository;

    private final RedisTemplate<String,Object> redisTemplate;

    public SubmissionService(SubmissionRepository submissionRepository, ProblemRepository problemRepository, ContestProblemRepository contestProblemRepository, RedisTemplate<String, Object> redisTemplate) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;
        this.contestProblemRepository = contestProblemRepository;
        this.redisTemplate = redisTemplate;
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
            contestProblem=contestProblemRepository.findById(pid).orElse(null);
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
            problem = problemRepository.findById(pid).orElse(null);
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
        assert submission != null;
        Long cid=submission.getContestId();
        Long pid=submission.getProblemId();
        ContestProblem contestProblem;
        Problem problem;
        if(cid!=0){//赛题
            contestProblem=contestProblemRepository.findById(pid).orElse(null);
            problem=contestProblem.getProblem();
        }
        else {
            problem = problemRepository.findById(pid).orElse(null);
        }
        //TODO 根据detail修改评测结果，待完善，需通知前端，修改榜单/题目统计AC数等
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
    }
}
