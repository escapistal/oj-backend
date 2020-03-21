package com.xc.oj.repository;

import com.xc.oj.entity.SubmissionBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionBaseRepository extends JpaRepository<SubmissionBase, Long> {
}
