package com.xc.oj.service;

import com.xc.oj.entity.*;
import com.xc.oj.repository.ContestRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContestService {
    private final ContestRepository contestRepository;

    public ContestService(ContestRepository contestRepository) {
        this.contestRepository = contestRepository;
    }

    public boolean existsById(Long id) {
        return contestRepository.existsById(id);
    }

    public Optional<Contest> findById(Long id){
        return contestRepository.findById(id);
    }

    public void save(Contest contest){
        contestRepository.save(contest);
    }

    public responseBase<Page<Contest>> list(boolean checkVisible, String keyword, String state, int page, int size){
        Page<Contest> contests = null;
        PageRequest pageRequest=null;
        if(!checkVisible&&!AuthUtil.has("admin"))
            checkVisible=true;
        boolean finalCheckVisible = checkVisible;
        Specification<Contest> specification= (Specification<Contest>) (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate=criteriaBuilder.and();
            if(finalCheckVisible)
                predicate=criteriaBuilder.and(predicate,criteriaBuilder.equal(root.get("visible"),true));
            if(keyword!=null&&!"".equals(keyword.trim()))
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + keyword.trim() + "%"));
            if("current".equals(state))
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThan(root.get("endTime"),new Timestamp(new Date().getTime())));
            else if("ended".equals(state))
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThan(root.get("endTime"),new Timestamp(new Date().getTime())));
            return predicate;
        };
        if("complete".equals(state)||"ended".equals(state)) {
            pageRequest=PageRequest.of(page,size, Sort.by(Sort.Order.asc("sortId"),Sort.Order.desc("startTime")));
        }
        else if("current".equals(state)){
            pageRequest=PageRequest.of(0,50, Sort.by(Sort.Order.asc("startTime")));
        }
        contests=contestRepository.findAll(specification,pageRequest);
        contests.forEach(c->{
            if(c.getPassword()!=null&&!c.getPassword().isEmpty())
                c.setPassword("set");
            else
                c.setPassword(null);
        });
        return responseBuilder.success(contests);
    }

    public responseBase<Contest> get(Long id) {
        Contest contest=contestRepository.findById(id).orElse(null);
        if(contest==null)
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
//        if(!contest.getVisible()&&!AuthUtil.has("admin"))
//            return responseBuilder.fail(responseCode.FORBIDDEN);
        return responseBuilder.success(contest);
    }

    public responseBase<String> add(Contest contest){
        contest.setCreateUser(new UserInfo(AuthUtil.getId()));
        contest.setCreateTime(new Timestamp(new Date().getTime()));
        contest.setUpdateUser(contest.getCreateUser());
        contest.setUpdateTime(contest.getCreateTime());
        contestRepository.save(contest);
        return responseBuilder.success(String.valueOf(contest.getId()));
    }

    public responseBase<String> update(Long id, Contest contest) {
        Contest data=contestRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        if(contest.getSortId()!=null)
            data.setSortId(contest.getSortId());
        if(contest.getTitle()!=null)
            data.setTitle(contest.getTitle());
        if(contest.getDescription()!=null)
            data.setDescription(contest.getDescription());
        if(contest.getPassword()!=null)
            data.setPassword(contest.getPassword());
        if(contest.getPenaltyTime()!=null && contest.getPenaltyTime() >= 0)
            data.setPenaltyTime(contest.getPenaltyTime());
        if(contest.getLockTime()!=null)
            data.setLockTime(contest.getLockTime());
        if(contest.getRuleType()!=null)
            data.setRuleType(contest.getRuleType());
        if(contest.getUnlockTime()!=null)
            data.setUnlockTime(contest.getUnlockTime());
        if(contest.getStartTime()!=null)
            data.setStartTime(contest.getStartTime());
        if(contest.getEndTime()!=null)
            data.setEndTime(contest.getEndTime());
        contest.setUpdateUser(new UserInfo(AuthUtil.getId()));
        contest.setUpdateTime(new Timestamp(new Date().getTime()));
        contestRepository.save(data);
        return responseBuilder.success();
    }

    public responseBase<String> delete(Long id){
        if(!contestRepository.existsById(id))
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        contestRepository.deleteById(id);
        return responseBuilder.success();
    }


}
