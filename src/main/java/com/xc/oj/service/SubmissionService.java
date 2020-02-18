package com.xc.oj.service;

import com.xc.oj.entity.ContestProblem;
import com.xc.oj.entity.JudgeTask;
import com.xc.oj.entity.Problem;
import com.xc.oj.entity.Submission;
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
    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ContestProblemRepository contestProblemRepository;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public responseBase<String> submit(Submission submission){
        submission.setStatus(0);
        submission.setCodeLength(submission.getCode().length());
        submission.setDetail(new ArrayList<>());
        submission.setCreateTime(new Timestamp(new Date().getTime()));
        submission.setExecuteTime(0);
        submission.setExecuteMemory(0);
        if(submission.getContestId()==null)
            submission.setContestId(0L);

        JudgeTask judgeTask=new JudgeTask();
        judgeTask.setSubmissionId(submission.getId());
        judgeTask.setLanguage(submission.getLanguage());
        judgeTask.setCode(submission.getCode());
        Long pid=submission.getProblemId();
        if(submission.getContestId()!=0) {
            ContestProblem contestProblem=contestProblemRepository.findById(pid).orElse(null);
            if(contestProblem==null)
                return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
            pid = contestProblem.getProblem().getId();
        }
        Problem problem=problemRepository.findById(pid).orElse(null);
        if(problem==null)
            return responseBuilder.fail(responseCode.PROBLEM_NOT_EXIST);
        submissionRepository.save(submission);
        judgeTask.setTestcaseMd5(problem.getTestCaseMd5());
        if(problem.getSpj())
            judgeTask.setSpjMd5(problem.getSpjMd5());
        redisTemplate.opsForList().leftPush("JudgeTask",judgeTask);
        return responseBuilder.success();
    }

    public responseBase<List<Submission>> listAll() {
        return responseBuilder.success(submissionRepository.findAll());
    }
}
