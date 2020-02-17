package com.xc.oj.controller;

import com.xc.oj.entity.ContestProblem;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contestProblem")
public class ContestProblemController {
    @Autowired
    ContestProblemService contestProblemService;

    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public responseBase<List<ContestProblem>> findByContestId(@PathVariable Long cid){
        return contestProblemService.findByContestId(cid);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public responseBase<ContestProblem> findById(@PathVariable Long id){
        return contestProblemService.findById(id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable Long id, @RequestBody ContestProblem contestProblem){
        return contestProblemService.update(id,contestProblem);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id) {
        return contestProblemService.delete(id);
    }
}
