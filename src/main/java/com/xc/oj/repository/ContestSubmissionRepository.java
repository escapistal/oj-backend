package com.xc.oj.repository;

import com.xc.oj.entity.ContestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContestSubmissionRepository extends JpaRepository<ContestSubmission, Long>, JpaSpecificationExecutor<ContestSubmission> {


}
