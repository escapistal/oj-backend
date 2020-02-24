package com.xc.oj.repository;

import com.xc.oj.entity.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long> {
    List<Announcement> findByVisible(Boolean visible);
}
