package com.xc.oj.repository;

import com.xc.oj.entity.Clarification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClarificationRepository extends JpaRepository<Clarification,Long> {

}
