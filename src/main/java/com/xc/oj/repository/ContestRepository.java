package com.xc.oj.repository;

import com.xc.oj.entity.Contest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContestRepository extends JpaRepository<Contest,Long> {

}
