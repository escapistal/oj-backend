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
import org.springframework.stereotype.Service;

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

    public responseBase<Page<Contest>> list(boolean checkVisible,String state, int page, int size){
        Page<Contest> contests = null;
        PageRequest pageRequest=null;
        if(!checkVisible&&!AuthUtil.has("admin"))
            checkVisible=true;
        if("complete".equals(state)) {
            pageRequest=PageRequest.of(page,size, Sort.by(Sort.Order.asc("sortId"),Sort.Order.desc("startTime")));
            if (!checkVisible)
                contests = contestRepository.findAll(pageRequest);
            else
                contests = contestRepository.findByVisible(true, pageRequest);
        }
        else if("current".equals(state)){
            pageRequest=PageRequest.of(0,50, Sort.by(Sort.Order.asc("startTime")));
            System.out.println(new Timestamp(new Date().getTime()));
            contests=contestRepository.findByEndTimeAfterAndVisible(new Timestamp(new Date().getTime()),true,pageRequest);
        }
        else if("ended".equals(state)){
            pageRequest=PageRequest.of(0,50, Sort.by(Sort.Order.asc("sortId"),Sort.Order.desc("startTime")));
            contests=contestRepository.findByEndTimeBeforeAndVisible(new Timestamp(new Date().getTime()),true,pageRequest);
        }
        contests.forEach(c->{
            System.out.println(c.getEndTime());
            System.out.println(c.getEndTime().before(new Timestamp(new Date().getTime())));
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
        return responseBuilder.success(contest);
    }

    public responseBase<String> add(Contest contest){
        contest.setCreateUser(new UserInfo(AuthUtil.getId()));
        contest.setCreateTime(new Timestamp(new Date().getTime()));
        contest.setUpdateUser(contest.getCreateUser());
        contest.setUpdateTime(contest.getCreateTime());
        contestRepository.save(contest);
        return responseBuilder.success();
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
        return responseBuilder.success();
    }

    public responseBase<String> delete(Long id){
        if(!contestRepository.existsById(id))
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        contestRepository.deleteById(id);
        return responseBuilder.success();
    }


}
