package com.xc.oj.repository;

import com.xc.oj.entity.ProblemBase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProblemBaseRepository extends JpaRepository<ProblemBase,Long> {

}
