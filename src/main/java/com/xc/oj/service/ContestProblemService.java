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

@Service
public class ContestProblemService {
    @Autowired
    ContestProblemRepository contestProblemRepository;

    public responseBase<List<ContestProblem>> findByContestId(long id){
        return responseBuilder.success(contestProblemRepository.findByContestId(id));
    }

    public responseBase<String> add(long cid,ContestProblem contestProblem){
        contestProblem.setCreateTime(new Timestamp(new Date().getTime()));
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

    public responseBase<ContestProblem> findById(Long id) {
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
