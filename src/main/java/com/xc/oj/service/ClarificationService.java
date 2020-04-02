package com.xc.oj.service;

import com.xc.oj.entity.Clarification;
import com.xc.oj.entity.ClarificationReply;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.repository.ClarificationReplyRepository;
import com.xc.oj.repository.ClarificationRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ClarificationService {

    private final ContestService contestService;
    private final ContestProblemService contestProblemService;
    private final ClarificationRepository clarificationRepository;
    private final ClarificationReplyRepository clarificationReplyRepository;

    public ClarificationService(ContestService contestService, ContestProblemService contestProblemService, ClarificationRepository clarificationRepository, ClarificationReplyRepository clarificationReplyRepository) {
        this.contestService = contestService;
        this.contestProblemService = contestProblemService;
        this.clarificationRepository = clarificationRepository;
        this.clarificationReplyRepository = clarificationReplyRepository;
    }


    public responseBase<Clarification> get(Long id) {
        Clarification data=clarificationRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CLARIFICATION_NOT_EXIST);
        boolean isAdmin=AuthUtil.has("admin"),updated=false;
        if(!isAdmin&&!data.getReadByUser()) {
            data.setReadByUser(true);
            updated=true;
        }
        else if(!isAdmin&&!data.getReadByAdmin()) {
            data.setReadByAdmin(true);
            updated=true;
        }
        if(updated)
            clarificationRepository.save(data);
        return responseBuilder.success(data);
    }

    public responseBase<String> add(Clarification clarification) {
        if(clarification.getProblem()!=null&&clarification.getProblem().getId()!=null) {
            ContestProblem problem = contestProblemService.findById(clarification.getProblem().getId()).orElse(null);
            if(problem==null)
                return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
            clarification.setContestId(problem.getContest().getId());
        }
        else if(clarification.getContestId()!=null){
            if(!contestService.existsById(clarification.getContestId()))
                return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        }
        else
            return responseBuilder.fail(responseCode.FORBIDDEN);
        clarification.setCreateTime(new Timestamp(new Date().getTime()));
        clarification.setCreateUser(new UserInfo(AuthUtil.getId()));
        clarification.setReply(new ArrayList<>());
        clarification.setReadByAdmin(false);
        clarification.setReadByUser(true);
        clarificationRepository.save(clarification);
        return responseBuilder.success();
    }

    public responseBase<String> reply(ClarificationReply clarificationReply) {
        System.out.println(clarificationReply.getClarId());
        System.out.println(clarificationReply.getContent());
        Clarification clarification=clarificationRepository.findById(clarificationReply.getClarId()).orElse(null);
        if(clarification==null)
            return responseBuilder.fail(responseCode.CLARIFICATION_NOT_EXIST);
        boolean isAdmin=AuthUtil.has("admin");
        if(!isAdmin&&AuthUtil.getId()!=clarification.getCreateUser().getId())
            return responseBuilder.fail(responseCode.FORBIDDEN);
        clarificationReply.setCreateTime(new Timestamp(new Date().getTime()));
        clarificationReply.setCreateUser(new UserInfo(AuthUtil.getId()));
        clarification.setReadByAdmin(isAdmin);
        clarification.setReadByUser(!isAdmin);
        clarificationRepository.save(clarification);
        clarificationReplyRepository.save(clarificationReply);
        return responseBuilder.success();
    }

    public responseBase<List<Clarification>> findByContestId(Long cid) {
        List<Clarification> clarifications;
        if(AuthUtil.has("admin"))
            clarifications=clarificationRepository.findByContestId(cid);
        else
            clarifications=clarificationRepository.findByContestIdAndCreateUser(cid,new UserInfo(AuthUtil.getId()));
        //TODO 懒加载
        clarifications.forEach(c->c.setReply(null));
//        Collections.sort(clarifications);
        return responseBuilder.success(clarifications);
    }
}
