package com.xc.oj.repository;

import com.xc.oj.entity.AcmContestRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AcmContestRankRepository extends JpaRepository<AcmContestRank,Long> {
    Optional<AcmContestRank> findByContestIdAndUserIdAndLocked(Long contestId, Long userId, Boolean locked);
    List<AcmContestRank> findByContestIdAndLocked(Long contestId,Boolean locked);
}
