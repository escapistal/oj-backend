package com.xc.oj.service;

import com.xc.oj.entity.Contest;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.repository.ContestRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContestService {
    @Autowired
    ContestRepository contestRepository;
    @Autowired
    ContestProblemService contestProblemService;

    public responseBase<List<Contest>> listAll(){
        return responseBuilder.success(contestRepository.findAll());
    }

    public responseBase<String> add(Contest contest){
        contest.setCreateTime(new Timestamp(new Date().getTime()));
        contest.setProblemList(new ArrayList<>());
        System.out.println(contest.getLockTime());
        contestRepository.save(contest);
        return responseBuilder.success();
    }

    public responseBase<String> deleteById(Long id){
        Contest contest=contestRepository.findById(id).orElse(null);
        if(contest==null)
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        return responseBuilder.success();
    }

    public responseBase<String> addProblem(Long id, ContestProblem contestProblem) {
        Contest contest=contestRepository.findById(id).orElse(null);
        if(contest==null)
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        contestProblemService.add(id,contestProblem);
        return responseBuilder.success();
    }

    public responseBase<String> update(Long id, Contest contest) {
        Contest data=contestRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        if(contest.getSortId()!=0)
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


        return responseBuilder.success();
    }
}
