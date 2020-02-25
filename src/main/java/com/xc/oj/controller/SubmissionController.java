package com.xc.oj.controller;

import com.xc.oj.entity.Submission;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.SubmissionService;
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

    @RequestMapping(value = {"/list","list/{cid}"}, method = RequestMethod.GET)
    public responseBase<List<Submission>> list(@PathVariable(required = false) Long cid){
        return submissionService.list(cid);
    }

    @PostAuthorize("returnObject.data.id == principal.id or hasAuthority('admin')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Submission> get(@PathVariable Long id){
        return submissionService.get(id);
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public responseBase<String> submit(@RequestBody Submission submission) {
        return submissionService.submit(submission);
    }
}
