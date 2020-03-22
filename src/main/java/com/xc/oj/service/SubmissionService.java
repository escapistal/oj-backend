package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.*;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final ContestService contestService;
    private final ContestProblemService contestProblemService;
    private final ProblemService problemService;
    private final UserService userService;
    private final AcmContestRankService acmContestRankService;
    private final CommonService commonService;
    private final RedisTemplate<String,Object> redisTemplate;

    public SubmissionService(SubmissionRepository submissionRepository, ContestService contestService, ContestProblemService contestProblemService, ProblemService problemService, UserService userService, AcmContestRankService acmContestRankService, CommonService commonService, RedisTemplate<String, Object> redisTemplate) {
        this.submissionRepository = submissionRepository;
        this.contestService = contestService;
        this.contestProblemService = contestProblemService;
        this.problemService = problemService;
        this.userService = userService;
        this.acmContestRankService = acmContestRankService;
        this.commonService = commonService;
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
        return commonService.submit(submission);
    }

}


