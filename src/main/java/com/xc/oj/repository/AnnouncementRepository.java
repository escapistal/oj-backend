package com.xc.oj.repository;

import com.xc.oj.entity.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement,Long>, JpaSpecificationExecutor<Announcement> {
    Page<Announcement> findAll(Pageable pageable);
    Page<Announcement> findByVisible(Boolean visible, Pageable pageable);
}
