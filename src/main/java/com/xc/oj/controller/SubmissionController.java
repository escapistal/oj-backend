package com.xc.oj.controller;

import com.xc.oj.entity.JudgeResultEnum;
import com.xc.oj.entity.Submission;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.SubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/submission")
public class SubmissionController {
    private final SubmissionService submissionService;

    public SubmissionController(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<Page<Submission>> list(
            @RequestParam(required = false) List<Long> pid,
            @RequestParam(required = false) Long uid,
            @RequestParam(required = false) String uname,
            @RequestParam(required = false) List<JudgeResultEnum> status,
            @RequestParam(required = false) List<String> lang,
            @RequestParam int page,
            @RequestParam int size
    ){
        if(pid==null)pid=new ArrayList<>();
        if("".equals(uid))uid=null;
        if("".equals(uname))uname=null;
        if(status==null)status=new ArrayList<>();
        if(lang==null)lang=new ArrayList<>();
        return submissionService.list(pid,uid,uname,status,lang,page,size);
    }

    @PostAuthorize("returnObject.status !=0 or returnObject.data.user.id == principal.id or hasAuthority('admin')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Submission> get(@PathVariable Long id){
        return submissionService.get(id);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public responseBase<String> submit(@RequestBody Submission submission) {
        return submissionService.submit(submission);
    }
}
