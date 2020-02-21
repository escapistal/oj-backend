package com.xc.oj.controller;

import com.xc.oj.entity.Submission;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.SubmissionService;
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
    public responseBase<List<Submission>> listAll(){
        return submissionService.listAll();
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public responseBase<String> submit(@RequestBody Submission submission) {
        return submissionService.submit(submission);
    }
}
