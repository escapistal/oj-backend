package com.xc.oj.controller;

import com.xc.oj.entity.Announcement;
import com.xc.oj.response.responseBase;
import com.xc.oj.service.AnnouncementService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {
    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public responseBase<List<Announcement>> list(){
        return announcementService.list();
    }

    @PostAuthorize("returnObject.data.visible == true or hasAuthority('admin')")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public responseBase<Announcement> get(@PathVariable Long id){
        return announcementService.get(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    public responseBase<String> add(@RequestBody Announcement announcement){
        return announcementService.add(announcement);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public responseBase<Announcement> update(@PathVariable Long id, @RequestBody Announcement announcement){
        return announcementService.update(id,announcement);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/changeVisible/{id}",method = RequestMethod.POST)
    public responseBase<String> changeVisible(@PathVariable Long id){
        return announcementService.changeVisible(id);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.POST)
    public responseBase<String> delete(@PathVariable Long id){
        return announcementService.delete(id);
    }
}
