package com.xc.oj.config;

import com.alibaba.fastjson.JSON;
import com.xc.oj.response.responseBuilder;
import com.xc.oj.response.responseCode;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        httpServletResponse.setStatus(HttpStatus.OK.value());
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(JSON.toJSONString(responseBuilder.fail(responseCode.LOGIN_FAIL)));
        writer.flush();
        writer.close();
    }
}
