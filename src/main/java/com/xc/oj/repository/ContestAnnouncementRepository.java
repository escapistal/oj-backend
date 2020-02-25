package com.xc.oj.repository;

import com.xc.oj.entity.ContestAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestAnnouncementRepository extends JpaRepository<ContestAnnouncement,Long> {
    List<ContestAnnouncement> findByContestId(Long id);

    List<ContestAnnouncement> findByContestIdAndVisible(Long cid, boolean visible);
}
