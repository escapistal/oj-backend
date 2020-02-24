package com.xc.oj.controller;

import com.xc.oj.entity.Announcement;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.AnnouncementService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public responseBase<List<Announcement>> listAll(){
        return announcementService.listAll(true);
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Announcement> get(@PathVariable Long id){
        return announcementService.get(id);
    }
}
