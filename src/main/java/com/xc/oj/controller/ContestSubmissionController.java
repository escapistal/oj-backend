package com.xc.oj.controller;

import com.xc.oj.entity.ContestSubmission;
import com.xc.oj.entity.JudgeResultEnum;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contestSubmission")
public class ContestSubmissionController {

    @Autowired
    ContestSubmissionService contestSubmissionService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<Page<ContestSubmission>> list(
            @RequestParam(required = false) Long cid,
            @RequestParam(required = false) Long pid,
            @RequestParam(required = false) Long uid,
            @RequestParam(required = false) String uname,
            @RequestParam(required = false) JudgeResultEnum status,
            @RequestParam(required = false) String lang,
            @RequestParam int page,
            @RequestParam int size
    ){
        if("".equals(cid))cid=null;
        if("".equals(pid))pid=null;
        if("".equals(uid))uid=null;
        if("".equals(uname))uname=null;
        if("".equals(lang))lang=null;
        return contestSubmissionService.list(cid,pid,uid,uname,status,lang,page,size);
    }

    @PostAuthorize("returnObject.status !=0 or returnObject.data.user.id == principal.id or hasAuthority('admin')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<ContestSubmission> get(@PathVariable Long id){
        return contestSubmissionService.get(id);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public responseBase<String> submit(@RequestBody ContestSubmission submission) {
        return contestSubmissionService.submit(submission);
    }

}
