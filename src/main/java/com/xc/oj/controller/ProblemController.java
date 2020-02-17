package com.xc.oj.controller;

import com.xc.oj.entity.Problem;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ProblemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@RestController
@RequestMapping("/api/problem")
public class ProblemController {
    @Autowired
    ProblemService problemService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public responseBase<List<Problem>> list_visible() {
        return problemService.list();
    }

    @RequestMapping(value = "/listAll", method = RequestMethod.GET)
    public responseBase<List<Problem>> list() {
        return problemService.listAll();
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Problem> findById(@PathVariable long id){
        return problemService.findById(id);
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Problem problem) {
        return problemService.add(problem);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable long id, @RequestBody Problem problem) {
        return problemService.update(id,problem);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable long id) {
        return problemService.delete(id);
    }

    @RequestMapping(value = "/changeVisible/{id}", method = RequestMethod.POST)
    public responseBase<String> changeVisible(@PathVariable long id) {
        return problemService.changeVisible(id);
    }

    @RequestMapping(value = "/setTestcase/{id}", method = RequestMethod.POST)
    public responseBase setTestcase(@PathVariable long id, @RequestBody MultipartFile file){
        return problemService.setTestcase(id,file);
    }
}
