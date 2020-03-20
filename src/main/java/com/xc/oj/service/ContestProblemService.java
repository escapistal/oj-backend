package com.xc.oj.service;

import com.xc.oj.entity.Contest;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.repository.ContestProblemRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContestProblemService {
    private final ContestService contestService;
    private final ContestProblemRepository contestProblemRepository;

    public ContestProblemService(ContestService contestService, ContestProblemRepository contestProblemRepository) {
        this.contestService = contestService;
        this.contestProblemRepository = contestProblemRepository;
    }

    public Optional<ContestProblem> findById(Long id){
        return contestProblemRepository.findById(id);
    }

    public void save(ContestProblem contestProblem){
        contestProblemRepository.save(contestProblem);
    }

    public responseBase<List<ContestProblem>> findByContestId(long cid){
        Contest contest=contestService.findById(cid).orElse(null);
        if(contest==null)
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        List<ContestProblem> contestProblems;
        if(AuthUtil.has("admin"))
            contestProblems=contestProblemRepository.findByContestId(cid);
        else {
            Timestamp now=new Timestamp(new Date().getTime());
            if(now.before(contest.getStartTime()))
                return responseBuilder.fail(responseCode.CONTEST_NOT_STARTED);
            contestProblems = contestProblemRepository.findByContestIdAndVisible(cid, true);
        }
        return responseBuilder.success(contestProblems);
    }

    public responseBase<ContestProblem> get(Long id) {
        ContestProblem contestProblem=contestProblemRepository.findById(id).orElse(null);
        if(contestProblem==null)
            return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
        if(!AuthUtil.has("admin")) {
            Contest contest=contestService.findById(contestProblem.getContestId()).orElse(null);
            Timestamp now=new Timestamp(new Date().getTime());
            if(now.before(contest.getStartTime()))
                return responseBuilder.fail(responseCode.CONTEST_NOT_STARTED);
        }
        return responseBuilder.success(contestProblem);
    }

    public responseBase<String> add(ContestProblem contestProblem){
        if(contestProblem.getContestId()==null||!contestService.existsById(contestProblem.getContestId()))
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        contestProblem.setCreateUser(new UserInfo(AuthUtil.getId()));
        contestProblem.setCreateTime(new Timestamp(new Date().getTime()));
        contestProblem.setUpdateUser(contestProblem.getCreateUser());
        contestProblem.setUpdateTime(contestProblem.getCreateTime());
        contestProblem.setAcceptedNumber(0);
        contestProblem.setSubmissionNumber(0);
        contestProblem.setAcceptedNumberLocked(0);
        contestProblem.setSubmissionNumberLocked(0);
        if(contestProblem.getVisible()==null)
            contestProblem.setVisible(true);
        if(contestProblem.getTimeLimit()==null)
            contestProblem.setTimeLimit(0);
        if(contestProblem.getMemoryLimit()==null)
            contestProblem.setMemoryLimit(0);
        contestProblemRepository.save(contestProblem);
        return responseBuilder.success();
    }

    public responseBase<String> update(long id, ContestProblem contestProblem){
        ContestProblem data=contestProblemRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
        if(contestProblem.getAllowLanguage()!=null)
            data.setAllowLanguage(contestProblem.getAllowLanguage());
        if(contestProblem.getProblem()!=null)
            data.setProblem(contestProblem.getProblem());
        if(contestProblem.getName()!=null)
            data.setName(contestProblem.getName());
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
