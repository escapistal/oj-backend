package com.xc.oj.controller;

import com.xc.oj.entity.Contest;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contest")
public class ContestController {
    @Autowired
    ContestService contestService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<List<Contest>> listAll() {
        return contestService.listAll();
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Contest contest){
        return contestService.add(contest);
    }

    @RequestMapping(value="/{id}/addProblem",method = RequestMethod.POST)
    public responseBase<String> addProblem(@PathVariable Long id, @RequestBody ContestProblem contestProblem){
        return contestService.addProblem(id,contestProblem);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable Long id, @RequestBody Contest contest){
        return contestService.update(id,contest);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id) {
        return contestService.deleteById(id);
    }
}
