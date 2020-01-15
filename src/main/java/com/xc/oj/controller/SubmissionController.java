package com.xc.oj.controller;

import com.xc.oj.entity.Submission;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.SubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/submission")
public class SubmissionController {
    @Autowired
    SubmissionService submissionService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<List<Submission>> listAll(){
        return submissionService.listAll();
    }

    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public responseBase<String> submit(@RequestBody Submission submission) {
        return submissionService.submit(submission);
    }
}
