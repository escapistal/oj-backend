package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.*;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.persistence.criteria.Predicate;

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

    public responseBase<Page<Submission>> list(List<Long> pids, Long uid, String uname,
                                               List<JudgeResultEnum> status, List<String> lang,
                                               int page, int size) {
        pids.forEach(pid-> System.out.println(pid));
        Page<Submission> submissions;
        Specification<Submission> specification= (Specification<Submission>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate=criteriaBuilder.and();
            if(!pids.isEmpty()) {
                Predicate p=criteriaBuilder.equal(root.get("problem").get("id"), pids.get(0));
                for (int i=1;i<pids.size();i++)
                    p = criteriaBuilder.or(p, criteriaBuilder.equal(root.get("problem").get("id"), pids.get(i)));
                predicate = criteriaBuilder.and(predicate, p);
            }
            if(uid!=null){
                predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("user").get("id"),uid));
            }
            else if(uname!=null){
                Predicate p=criteriaBuilder.or(criteriaBuilder.like(root.get("user").get("nickname"),"%"+uname+"%"),
                        criteriaBuilder.like(root.get("user").get("realname"),"%"+uname+"%"));
                predicate=criteriaBuilder.and(predicate,p);
            }
            if(!status.isEmpty()) {
                Predicate p=criteriaBuilder.equal(root.get("status"), status.get(0));
                for (int i=1;i<status.size();i++)
                    p = criteriaBuilder.or(p, criteriaBuilder.equal(root.get("status"), status.get(i)));
                predicate = criteriaBuilder.and(predicate, p);
            }
            if(!lang.isEmpty()){
                Predicate p=criteriaBuilder.equal(root.get("language"), lang.get(0));
                for (int i=1;i<lang.size();i++)
                    p = criteriaBuilder.or(p, criteriaBuilder.equal(root.get("language"), lang.get(i)));
                predicate = criteriaBuilder.and(predicate, p);
            }
            return predicate;
        };
        PageRequest pageRequest=PageRequest.of(page,size,Sort.by(Sort.Order.desc("createTime")));
        submissions = submissionRepository.findAll(specification,pageRequest);
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


