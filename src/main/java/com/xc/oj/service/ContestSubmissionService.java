package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.ContestSubmissionRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
public class ContestSubmissionService {

    final ContestSubmissionRepository contestSubmissionRepository;
    final ContestProblemService contestProblemService;
    final CommonService commonService;
    final ContestService contestService;

    public ContestSubmissionService(ContestSubmissionRepository contestSubmissionRepository, ContestProblemService contestProblemService, CommonService commonService, ContestService contestService) {
        this.contestSubmissionRepository = contestSubmissionRepository;
        this.contestProblemService = contestProblemService;
        this.commonService = commonService;
        this.contestService = contestService;
    }

    public Optional<ContestSubmission> findById(Long id){
        return contestSubmissionRepository.findById(id);
    }

    public void save(ContestSubmission contestSubmission){
        contestSubmissionRepository.save(contestSubmission);
    }

    public responseBase<Page<ContestSubmission>> list(Long cid, Long pid, Long uid, String uname, int page, int size) {
        //cid与pid至少提供1个，若不匹配，优先按pid所属的contest
        if(cid==null&&pid==null)
            return responseBuilder.fail(responseCode.FORBIDDEN);
        Page<ContestSubmission> contestSubmissions;
        ContestProblem problem;
        Timestamp start,end,lock;
        boolean willLock,realTimeRank,isAdmin=AuthUtil.has("admin");
        if(pid!=null) {
            problem = contestProblemService.findById(pid).orElse(null);
            if (problem == null || !problem.getVisible()&&isAdmin)
                return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
            cid=problem.getContest().getId();
            start=problem.getContest().getStartTime();
            end=problem.getContest().getEndTime();
            lock=problem.getContest().getLockTime();
            willLock=problem.getContest().getWillLock();
            realTimeRank=problem.getContest().getRealTimeRank();
        }
        else{//cid!=null
            Contest contest=contestService.findById(cid).orElse(null);
            if(contest==null || !contest.getVisible()&&!isAdmin)
                return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
            start=contest.getStartTime();
            end=contest.getEndTime();
            lock=contest.getLockTime();
            willLock=contest.getWillLock();
            realTimeRank=contest.getRealTimeRank();
        }
        Timestamp now=new Timestamp(new Date().getTime());
        if(!isAdmin&&now.before(start)){//赛前禁止非管理员获取
            return responseBuilder.fail(responseCode.CONTEST_NOT_STARTED);
        }
        Long finalCid = cid;
        Specification<ContestSubmission> specification= (Specification<ContestSubmission>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate=criteriaBuilder.equal(root.get("contest").get("id"), finalCid);
            if(pid!=null){
                predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("problem").get("id"),pid));
            }
            if(uid!=null){
                predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("user").get("id"),uid));
            }
            else if(uname!=null){
                Predicate p=criteriaBuilder.or(criteriaBuilder.like(root.get("user").get("nickname"),"%"+uname+"%"),
                        criteriaBuilder.like(root.get("user").get("realname"),"%"+uname+"%"));
                predicate=criteriaBuilder.and(predicate,p);
            }
            if(AuthUtil.has("admin")) {//管理员查询，无条件返回所有筛选下的数据
                return predicate;
            }
            if(!now.before(start)&&!now.after(end)) {//赛中
                if(!realTimeRank){//无实时榜单，赛中只能看到自己
                    predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("user").get("id"),AuthUtil.getId()));
                }
                else{//有实时榜单
                    if(willLock&&!now.before(lock)){//封榜阶段
                        Predicate p=criteriaBuilder.or(criteriaBuilder.equal(root.get("user").get("id"),AuthUtil.getId()),
                                criteriaBuilder.lessThan(root.get("createTime"),lock));
                        predicate=criteriaBuilder.and(predicate,p);
                    }
                    //else赛中正常阶段，全都给看
                }
            }
            //else赛后，全部放开
            return predicate;
        };

        PageRequest pageRequest=PageRequest.of(page,size, Sort.by(Sort.Order.desc("createTime")));
        contestSubmissions = contestSubmissionRepository.findAll(specification,pageRequest);
        return responseBuilder.success(contestSubmissions);
    }

    public responseBase<String> submit(ContestSubmission submission) {
        return commonService.submit(submission);
    }

    public responseBase<ContestSubmission> get(Long id) {
        ContestSubmission submission=contestSubmissionRepository.findById(id).orElse(null);
        if(submission==null)
            return responseBuilder.fail(responseCode.SUBMISSION_NOT_EXIST);
        return responseBuilder.success(submission);
    }
}
