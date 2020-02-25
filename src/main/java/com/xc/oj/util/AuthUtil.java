package com.xc.oj.util;

import com.xc.oj.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtil {
    public static boolean has(String role){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getRole().contains(role.toLowerCase());
    }

    public static User getUser(){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user;
    }

    public static long getId(){
        User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
