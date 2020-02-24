package com.xc.oj.service;

import com.xc.oj.entity.User;
import com.xc.oj.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JWTUserService implements UserDetailsService {

    private final UserRepository userRepository;

    public JWTUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<User> users;
        if(s.contains("@"))
            users=userRepository.findByEmail(s);
        else
            users=userRepository.findByUsername(s);
        if(users.isEmpty())
            throw new UsernameNotFoundException("用户名或密码错误");
        return users.get(0);
    }
}
