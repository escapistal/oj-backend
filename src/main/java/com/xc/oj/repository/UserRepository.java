package com.xc.oj.repository;

import com.xc.oj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    boolean existsByUsername(String username);
    List<User> findByUsername(String username);
    boolean existsByEmail(String email);
    List<User> findByEmail(String email);
}
