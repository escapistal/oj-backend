package com.xc.oj.controller;

import com.xc.oj.entity.ContestProblem;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestProblemService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contestProblem")
public class ContestProblemController {
    private final ContestProblemService contestProblemService;

    public ContestProblemController(ContestProblemService contestProblemService) {
        this.contestProblemService = contestProblemService;
    }

    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public responseBase<List<ContestProblem>> findByContestId(@PathVariable Long cid){
        return contestProblemService.findByContestId(cid);
    }

    @PostAuthorize("returnObject.data.visible == true or hasAuthority('admin')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public responseBase<ContestProblem> get(@PathVariable Long id){
        return contestProblemService.get(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody ContestProblem contestProblem){
        return contestProblemService.add(contestProblem);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable Long id, @RequestBody ContestProblem contestProblem){
        return contestProblemService.update(id,contestProblem);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id) {
        return contestProblemService.delete(id);
    }
}
