package com.xc.oj.repository;

import com.xc.oj.entity.ContestProblem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestProblemRepository extends JpaRepository<ContestProblem,Long> {
    List<ContestProblem> findByContestId(Long id);

}
