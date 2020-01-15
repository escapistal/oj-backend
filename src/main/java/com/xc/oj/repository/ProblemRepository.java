package com.xc.oj.repository;

import com.xc.oj.entity.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    List<Problem> findAll();
    List<Problem> findByVisibleTrue();
}
