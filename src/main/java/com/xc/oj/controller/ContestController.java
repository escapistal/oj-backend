package com.xc.oj.controller;

import com.xc.oj.entity.Contest;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contest")
public class ContestController {
    private final ContestService contestService;

    public ContestController(ContestService contestService) {
        this.contestService = contestService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<Page<Contest>> list(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false,defaultValue = "true") boolean checkVisible) {
        return contestService.list(checkVisible,page,size);
    }

    @PostAuthorize("returnObject.data.visible == true or hasAuthority('admin')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Contest> get(@PathVariable Long id){
        return contestService.get(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value="/add", method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Contest contest){
        return contestService.add(contest);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable Long id, @RequestBody Contest contest){
        return contestService.update(id,contest);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id) {
        return contestService.delete(id);
    }
}
