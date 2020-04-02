package com.xc.oj.service;

import com.xc.oj.entity.ContestAnnouncement;
import com.xc.oj.entity.ContestProblem;
import com.xc.oj.entity.UserInfo;
import com.xc.oj.repository.ContestAnnouncementRepository;
import com.xc.oj.response.responseBase;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import com.xc.oj.util.AuthUtil;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContestAnnouncementService {
    private final ContestService contestService;
    private final ContestProblemService contestProblemService;
    private final ContestAnnouncementRepository contestAnnouncementRepository;

    public ContestAnnouncementService(ContestService contestService, ContestProblemService contestProblemService, ContestAnnouncementRepository contestAnnouncementRepository) {
        this.contestService = contestService;
        this.contestProblemService = contestProblemService;
        this.contestAnnouncementRepository = contestAnnouncementRepository;
    }

    public Optional<ContestAnnouncement> findById(Long id){
        return contestAnnouncementRepository.findById(id);
    }

    public void save(ContestAnnouncement contestAnnouncement){
        contestAnnouncementRepository.save(contestAnnouncement);
    }

    public responseBase<List<ContestAnnouncement>> findByContestId(Long cid){
        List<ContestAnnouncement> contestAnnouncements;
        if(AuthUtil.has("admin"))
            contestAnnouncements=contestAnnouncementRepository.findByContestId(cid);
        else
            contestAnnouncements=contestAnnouncementRepository.findByContestIdAndVisible(cid,true);
//        contestAnnouncements.forEach(a->a.setContent(null));
        return responseBuilder.success(contestAnnouncements);
    }

    public responseBase<ContestAnnouncement> get(Long id) {
        ContestAnnouncement contestAnnouncement=contestAnnouncementRepository.findById(id).orElse(null);
        if(contestAnnouncement==null)
            return responseBuilder.fail(responseCode.CONTEST_ANNOUNCEMENT_NOT_EXIST);
        return responseBuilder.success(contestAnnouncement);
    }

    public responseBase<String> add(ContestAnnouncement contestAnnouncement){
        if(contestAnnouncement.getProblem()!=null&&contestAnnouncement.getProblem().getId()!=null) {
            ContestProblem problem = contestProblemService.findById(contestAnnouncement.getProblem().getId()).orElse(null);
            if(problem==null)
                return responseBuilder.fail(responseCode.CONTEST_PROBLEM_NOT_EXIST);
            contestAnnouncement.setContestId(problem.getContest().getId());
        }
        else if(contestAnnouncement.getContestId()!=null){
            if(!contestService.existsById(contestAnnouncement.getContestId()))
                return responseBuilder.fail(responseCode.CONTEST_NOT_EXIST);
        }
        else
            return responseBuilder.fail(responseCode.FORBIDDEN);
        if(contestAnnouncement.getVisible()==null)
            contestAnnouncement.setVisible(true);
        if(contestAnnouncement.getSortId()==null)
            contestAnnouncement.setSortId(0);
        contestAnnouncement.setCreateUser(new UserInfo(AuthUtil.getId()));
        contestAnnouncement.setCreateTime(new Timestamp(new Date().getTime()));
        contestAnnouncement.setUpdateUser(contestAnnouncement.getCreateUser());
        contestAnnouncement.setUpdateTime(contestAnnouncement.getCreateTime());
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
        data.setUpdateUser(new UserInfo(AuthUtil.getId()));
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
