package com.xc.oj.service;

import com.xc.oj.entity.ContestAnnouncement;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.repository.ContestAnnouncementRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Service
public class ContestAnnouncementService {
    @Autowired
    private ContestAnnouncementRepository contestAnnouncementRepository;
    public responseBase<List<ContestAnnouncement>> findByContestId(Long cid){
        return responseBuilder.success(contestAnnouncementRepository.findByContestId(cid));
    }

    public responseBase<ContestAnnouncement> findById(Long id) {
        ContestAnnouncement contestAnnouncement=contestAnnouncementRepository.findById(id).orElse(null);
        if(contestAnnouncement==null)
            return responseBuilder.fail(responseCode.CONTEST_ANNOUNCEMENT_NOT_EXIST);
        return responseBuilder.success(contestAnnouncement);
    }

    public responseBase<String> add(Long cid,ContestAnnouncement contestAnnouncement){
        if(contestAnnouncement.getVisible()==null)
            contestAnnouncement.setVisible(true);
        contestAnnouncementRepository.save(contestAnnouncement);
        return responseBuilder.success();
    }

    public responseBase<String> update(Long id, ContestAnnouncement contestAnnouncement) {
        ContestAnnouncement data=contestAnnouncementRepository.findById(id).orElse(null);
        if(data==null)
            return responseBuilder.fail(responseCode.CONTEST_ANNOUNCEMENT_NOT_EXIST);
        if(contestAnnouncement.getSortId()!=null)
            data.setSortId(contestAnnouncement.getSortId());
        if(contestAnnouncement.getTitle()!=null)
            data.setTitle(contestAnnouncement.getTitle());
        if(contestAnnouncement.getContent()!=null)
            data.setContent(contestAnnouncement.getContent());
        if(contestAnnouncement.getVisible()!=null)
            data.setVisible(contestAnnouncement.getVisible());
        data.setUpdateTime(new Timestamp(new Date().getTime()));
        contestAnnouncementRepository.save(data);
        return responseBuilder.success();
    }

    public responseBase<String> delete(Long id) {
        if(!contestAnnouncementRepository.existsById(id))
            return responseBuilder.fail(responseCode.CONTEST_ANNOUNCEMENT_NOT_EXIST);
        contestAnnouncementRepository.deleteById(id);
        return responseBuilder.success();
    }
}
