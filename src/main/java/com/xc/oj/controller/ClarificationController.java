package com.xc.oj.controller;

import com.xc.oj.entity.Clarification;
import com.xc.oj.entity.ClarificationReply;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ClarificationService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clarification")
public class ClarificationController {
    private final ClarificationService clarificationService;

    public ClarificationController(ClarificationService clarificationService) {
        this.clarificationService = clarificationService;
    }

    @RequestMapping(value = "/list/{cid}",method = RequestMethod.GET)//只获取当前用户的clar
    public responseBase<List<Clarification>> findByContestId(@PathVariable Long cid){
        return clarificationService.findByContestId(cid);
    }

    @PostAuthorize("returnObject.data.createUser.id==principal.id or hasAuthority('admin')")//只允许获取当前用户的clar详情
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Clarification> get(@PathVariable Long id) {
        return clarificationService.get(id);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Clarification clarification){
        return clarificationService.add(clarification);
    }

    @RequestMapping(value = "/reply",method = RequestMethod.POST)
    public responseBase<String> reply(@RequestBody ClarificationReply clarificationReply){
        return clarificationService.reply(clarificationReply);
    }

}
