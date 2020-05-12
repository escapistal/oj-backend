package com.xc.oj.controller;

import com.xc.oj.response.responseBase;
import com.xc.oj.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    private final CommonService commonService;

    public CommonController(CommonService commonService) {
        this.commonService = commonService;
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public responseBase upload(@RequestBody MultipartFile file){
        return commonService.upload(file);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/changeOption", method = RequestMethod.POST)
    public responseBase changeOption(@RequestParam String key, @RequestParam String value){
        return commonService.changeOption(key,value);
    }

    @PreAuthorize("hasAuthority('admin')")
    @RequestMapping(value = "/options", method = RequestMethod.GET)
    public  responseBase getOptions(){
        return commonService.getOptions();
    }
}
