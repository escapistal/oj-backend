package com.xc.oj.controller;

import com.xc.oj.entity.Problem;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/problem")
public class ProblemController {
    private final ProblemService problemService;

    public ProblemController(ProblemService problemService) {
        this.problemService = problemService;
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<Page<Problem>> list(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false,defaultValue = "true") boolean checkVisible) {
        return problemService.list(checkVisible,page,size);
    }

    @PostAuthorize("returnObject.data.visible == true or hasAuthority('admin')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Problem> get(@PathVariable long id){
        return problemService.get(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Problem problem) {
        return problemService.add(problem);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable long id, @RequestBody Problem problem) {
        return problemService.update(id,problem);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable long id) {
        return problemService.delete(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/changeVisible/{id}", method = RequestMethod.POST)
    public responseBase<String> changeVisible(@PathVariable long id) {
        return problemService.changeVisible(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/setTestcase/{id}", method = RequestMethod.POST)
    public responseBase setTestcase(@PathVariable long id, @RequestBody MultipartFile file){
        return problemService.setTestcase(id,file);
    }
}
