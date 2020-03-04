package com.xc.oj.controller;

import com.xc.oj.entity.Submission;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.SubmissionService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(required = false) Long cid,
            @RequestParam(required = false) Long pid,
            @RequestParam(required = false) Long uid,
            @RequestParam(required = false) String uname,
            @RequestParam int page,
            @RequestParam int size
            ){
        return submissionService.list(cid,pid,uid,uname,page,size);
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
