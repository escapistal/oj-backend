package com.xc.oj.controller;

import com.xc.oj.entity.ContestAnnouncement;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contestAnnouncement")
public class ContestAnnouncementController {
    @Autowired
    ContestAnnouncementService contestAnnouncementService;

    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public responseBase<List<ContestAnnouncement>> findByContestId(@PathVariable Long cid){
        return contestAnnouncementService.findByContestId(cid);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public responseBase<ContestAnnouncement> findById(@PathVariable Long id){
        return contestAnnouncementService.findById(id);
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable Long id, @RequestBody ContestAnnouncement contestAnnouncement){
        return contestAnnouncementService.update(id,contestAnnouncement);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id) {
        return contestAnnouncementService.delete(id);
    }
}
