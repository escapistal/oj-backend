package com.xc.oj.controller;

import com.xc.oj.entity.Announcement;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.AnnouncementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/announcement")
public class AdminAnnouncementController {
    private final AnnouncementService announcementService;

    public AdminAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public responseBase<List<Announcement>> listAll(){
        return announcementService.listAll(false);
    }

    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Announcement announcement){
        return announcementService.add(announcement);
    }

    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public responseBase<Announcement> update(@PathVariable Long id, @RequestBody Announcement announcement){
        return announcementService.update(id,announcement);
    }

    @RequestMapping(value = "/changeVisible/{id}",method = RequestMethod.POST)
    public responseBase<String> changeVisible(@PathVariable Long id){
        return announcementService.changeVisible(id);
    }

    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id){
        return announcementService.delete(id);
    }

}
