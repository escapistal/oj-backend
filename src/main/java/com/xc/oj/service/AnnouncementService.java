package com.xc.oj.service;

import com.xc.oj.entity.Announcement;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.repository.AnnouncementRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {
    private final AnnouncementRepository announcementRepository;

    public AnnouncementService(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    public responseBase<Page<Announcement>> list(boolean checkVisible,int page,int size) {
        Page<Announcement> announcements;
        PageRequest pageRequest=PageRequest.of(page, size, Sort.by(Sort.Order.asc("sortId"),Sort.Order.desc("createTime")));
        if(!checkVisible&&!AuthUtil.has("admin"))
            checkVisible=true;
        if(!checkVisible)
            announcements=announcementRepository.findAll(pageRequest);
        else
            announcements=announcementRepository.findByVisible(true, pageRequest);
        //TODO 懒加载
        announcements.forEach(a->a.setContent(null));
        return responseBuilder.success(announcements);
    }

    public responseBase<String> add(Announcement announcement) {
        announcement.setCreateTime(new Timestamp(new Date().getTime()));
        announcement.setCreateUser(new UserInfo(AuthUtil.getId()));
        announcement.setUpdateTime(announcement.getCreateTime());
        announcement.setUpdateUser(announcement.getCreateUser());
        announcementRepository.save(announcement);
        return responseBuilder.success();
    }

    public responseBase<Announcement> update(Long id, Announcement announcement) {
        Announcement data=announcementRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.ANNOUNCEMENT_NOT_EXIST);
        if(announcement.getSortId()!=null)
            data.setSortId(announcement.getSortId());
        if(announcement.getVisible()!=null)
            data.setVisible(announcement.getVisible());
        if(announcement.getContent()!=null)
            data.setContent(announcement.getContent());
        if(announcement.getTitle()!=null)
            data.setTitle(announcement.getTitle());
        data.setUpdateUser(new UserInfo(AuthUtil.getId()));
        data.setUpdateTime(new Timestamp(new Date().getTime()));
        announcementRepository.save(data);
        return responseBuilder.success(announcement);
    }

    public responseBase<String> delete(Long id){
        if(!announcementRepository.existsById(id))
            return responseBuilder.fail(responseCode.ANNOUNCEMENT_NOT_EXIST);
        announcementRepository.deleteById(id);
        return responseBuilder.success();
    }

    public responseBase<Announcement> get(Long id) {
        Announcement data=announcementRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.ANNOUNCEMENT_NOT_EXIST);
        return responseBuilder.success(data);
    }

    public responseBase<String> changeVisible(Long id) {
        Announcement data=announcementRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.ANNOUNCEMENT_NOT_EXIST);
        data.setVisible(!data.getVisible());
        announcementRepository.save(data);
        return responseBuilder.success();
    }
}
