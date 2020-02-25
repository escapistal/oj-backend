package com.xc.oj.service;

import com.xc.oj.entity.Clarification;
import com.xc.oj.entity.ClarificationReply;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.repository.ClarificationReplyRepository;
import com.xc.oj.repository.ClarificationRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import net.bytebuddy.dynamic.scaffold.MethodRegistry;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class ClarificationService {

    private final ContestService contestService;
    private final ClarificationRepository clarificationRepository;
    private final ClarificationReplyRepository clarificationReplyRepository;

    public ClarificationService(ContestService contestService, ClarificationRepository clarificationRepository, ClarificationReplyRepository clarificationReplyRepository) {
        this.contestService = contestService;
        this.clarificationRepository = clarificationRepository;
        this.clarificationReplyRepository = clarificationReplyRepository;
    }


    public responseBase<Clarification> get(Long id) {
        Clarification data=clarificationRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CLARIFICATION_NOT_EXIST);
        return responseBuilder.success(data);
    }

    public responseBase<Clarification> add(Clarification clarification) {
        if(clarification.getContestId()==null||!contestService.existsById(clarification.getContestId()))
            return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        clarification.setCreateTime(new Timestamp(new Date().getTime()));
        clarification.setCreateUser(new UserInfo(AuthUtil.getId()));
        clarification.setReply(new ArrayList<>());
        if(clarification.getProblemId()==null)
            clarification.setProblemId(0L);
        clarificationRepository.save(clarification);
        return responseBuilder.success(clarification);
    }

    public responseBase<String> reply(ClarificationReply clarificationReply) {
        Clarification clarification=clarificationRepository.findById(clarificationReply.getClarId()).orElse(null);
        if(clarification==null)
            return responseBuilder.fail(responseCode.CLARIFICATION_NOT_EXIST);
        if(!AuthUtil.has("admin")&&AuthUtil.getId()!=clarification.getCreateUser().getId())
            return responseBuilder.fail(responseCode.FORBIDDEN);
        clarificationReply.setCreateTime(new Timestamp(new Date().getTime()));
        clarificationReply.setCreateUser(new UserInfo(AuthUtil.getId()));
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
        Collections.sort(clarifications);
        return responseBuilder.success(clarifications);
    }
}