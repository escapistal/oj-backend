package com.xc.oj.repository;

import com.xc.oj.entity.Contest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest,Long> {

    Page<Contest> findByVisible(boolean visible, Pageable pageable);
}
