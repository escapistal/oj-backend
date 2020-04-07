package com.xc.oj.repository;

import com.xc.oj.entity.Clarification;
import com.xc.oj.entity.UserInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClarificationRepository extends JpaRepository<Clarification,Long> {
    List<Clarification> findByContestId(Long cid, Sort sort);
    List<Clarification> findByContestIdAndCreateUser(Long cid, UserInfo userInfo,Sort sort);
}
