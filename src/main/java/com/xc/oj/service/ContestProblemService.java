package com.xc.oj.service;

import com.xc.oj.entity.ContestProblem;
import com.xc.oj.repository.ContestProblemRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContestProblemService {
    private final ContestProblemRepository contestProblemRepository;

    public ContestProblemService(ContestProblemRepository contestProblemRepository) {
        this.contestProblemRepository = contestProblemRepository;
    }

    public Optional<ContestProblem> findById(Long id){
        return contestProblemRepository.findById(id);
    }

    public void save(ContestProblem contestProblem){
        contestProblemRepository.save(contestProblem);
    }

    public responseBase<List<ContestProblem>> findByContestId(long id){
        return responseBuilder.success(contestProblemRepository.findByContestId(id));
    }

    public responseBase<String> add(long cid,ContestProblem contestProblem){
        contestProblem.setCreateTime(new Timestamp(new Date().getTime()));
        contestProblem.setUpdateTime(new Timestamp(new Date().getTime()));
        contestProblem.setContestId(cid);
        contestProblem.setAcceptedNumber(0);
        contestProblem.setSubmissionNumber(0);
        contestProblem.setAcceptedNumberLocked(0);
        contestProblem.setSubmissionNumberLocked(0);
        if(contestProblem.getVisible()==null)
            contestProblem.setVisible(true);
        contestProblemRepository.save(contestProblem);
        return responseBuilder.success();
    }

    public responseBase<ContestProblem> get(Long id) {
        ContestProblem contestProblem=contestProblemRepository.findById(id).orElse(null);
        if(contestProblem==null)
            return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
        return responseBuilder.success(contestProblem);
    }

    public responseBase<String> update(long id, ContestProblem contestProblem){
        ContestProblem data=contestProblemRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
        if(contestProblem.getAllowLanguage()!=null)
            data.setAllowLanguage(contestProblem.getAllowLanguage());
        if(contestProblem.getProblem()!=null)
            data.setProblem(contestProblem.getProblem());
        if(contestProblem.getShortname()!=null)
            data.setShortname(contestProblem.getShortname());
        if(contestProblem.getSortId()!=null)
            data.setSortId(contestProblem.getSortId());
        if(contestProblem.getAllowLanguage()!=null)
            data.setAllowLanguage(contestProblem.getAllowLanguage());
        if(contestProblem.getTimeLimit()!=null)
            data.setTimeLimit(contestProblem.getTimeLimit());
        if(contestProblem.getMemoryLimit()!=null)
            data.setMemoryLimit(contestProblem.getMemoryLimit());
        if(contestProblem.getVisible()!=null)
            data.setVisible(contestProblem.getVisible());
        contestProblemRepository.save(data);
        return responseBuilder.success();
    }

    public responseBase<String> delete(Long id){
        if(!contestProblemRepository.existsById(id))
            return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
        contestProblemRepository.deleteById(id);
        return responseBuilder.success();
    }


}
