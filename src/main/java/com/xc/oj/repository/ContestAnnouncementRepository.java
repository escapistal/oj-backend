package com.xc.oj.repository;

import com.xc.oj.entity.ContestAnnouncement;
import com.xc.oj.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestAnnouncementRepository extends JpaRepository<ContestAnnouncement,Long> {
    List<ContestAnnouncement> findByContestId(Long id);
}
