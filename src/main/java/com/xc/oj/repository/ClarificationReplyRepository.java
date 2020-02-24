package com.xc.oj.repository;

import com.xc.oj.entity.ClarificationReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClarificationReplyRepository extends JpaRepository<ClarificationReply,Long> {

}
