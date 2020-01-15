package com.xc.oj.repository;

import com.xc.oj.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContestProblemRepository extends JpaRepository<ContestProblem,Long> {
    List<ContestProblem> findByContestId(Long id);

}
