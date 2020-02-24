package com.xc.oj.config;

import com.alibaba.fastjson.JSON;
import com.xc.oj.entity.User;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.util.JWTUtil;
import io.jsonwebtoken.Claims;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.OK.value());
        Map<String,Object> claims=new HashMap<>();
        User user=(User)(authentication.getPrincipal());
        claims.put("userId",user.getId());
        claims.put("userRole",user.getRole());
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(JSON.toJSONString(responseBuilder.success(JWTUtil.create(claims))));
        writer.flush();
        writer.close();
    }
}
