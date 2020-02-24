package com.xc.oj.config;

import com.xc.oj.entity.User;
import com.xc.oj.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = httpServletRequest.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                final String authToken = authHeader.substring("Bearer ".length());
                Long uid = ((Integer) (JWTUtil.getClaims(authToken).get("userId"))).longValue();
                List<String> role = (List) JWTUtil.getClaims(authToken).get("userRole");
                if (uid != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User user = new User();
                    user.setId(uid);
                    user.setRole(role);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }catch(ExpiredJwtException e){
                //如果过期则正常按未携带token处理
            }catch(UsernameNotFoundException e){
                //如果用户名不存在则正常按未携带token处理
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }
}
