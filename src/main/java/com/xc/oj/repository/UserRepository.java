package com.xc.oj.repository;

import com.xc.oj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAll();
    List<User> findByUsernameAndPassword(String username, String encodedPassword);
    boolean existsByUsernameAndPassword(String username, String encodedPassword);
    boolean existsByUsername(String username);
    List<User> findByUsername(String username);
    List<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
