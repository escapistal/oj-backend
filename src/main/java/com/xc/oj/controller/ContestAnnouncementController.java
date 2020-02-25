package com.xc.oj.controller;

import com.xc.oj.entity.ContestAnnouncement;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.ContestAnnouncementService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contestAnnouncement")
public class ContestAnnouncementController {
    private final ContestAnnouncementService contestAnnouncementService;

    public ContestAnnouncementController(ContestAnnouncementService contestAnnouncementService) {
        this.contestAnnouncementService = contestAnnouncementService;
    }

    @RequestMapping(value = "/list/{cid}", method = RequestMethod.GET)
    public responseBase<List<ContestAnnouncement>> findByContestId(@PathVariable Long cid){
        return contestAnnouncementService.findByContestId(cid);
    }

    @PostAuthorize("returnObject.data.visible == true or hasAuthority('admin')")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public responseBase<ContestAnnouncement> get(@PathVariable Long id){
        return contestAnnouncementService.get(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody ContestAnnouncement contestAnnouncement){
        return contestAnnouncementService.add(contestAnnouncement);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public responseBase<String> update(@PathVariable Long id, @RequestBody ContestAnnouncement contestAnnouncement){
        return contestAnnouncementService.update(id,contestAnnouncement);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id) {
        return contestAnnouncementService.delete(id);
    }
}
