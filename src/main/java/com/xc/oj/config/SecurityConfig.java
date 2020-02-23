package com.xc.oj.config;

import com.xc.oj.service.JWTUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JWTUserService jwtUserService;
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthSuccessHandler authSuccessHandler;
    private final AuthFailureHandler authFailureHandler;
    private final LogoutHandler logoutHandler;
    private final AuthEntryPoint authEntryPoint;
    private final AuthAccessDeniedHandler authAccessDeniedHandler;

    public SecurityConfig(JWTUserService jwtUserService, JwtAuthFilter jwtAuthFilter, AuthSuccessHandler authSuccessHandler, AuthFailureHandler authFailureHandler, LogoutHandler logoutHandler, AuthEntryPoint authEntryPoint, AuthAccessDeniedHandler authAccessDeniedHandler) {
        this.jwtUserService = jwtUserService;
        this.jwtAuthFilter = jwtAuthFilter;
        this.authSuccessHandler = authSuccessHandler;
        this.authFailureHandler = authFailureHandler;
        this.logoutHandler = logoutHandler;
        this.authEntryPoint = authEntryPoint;
        this.authAccessDeniedHandler = authAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http
            .exceptionHandling()
            .authenticationEntryPoint(authEntryPoint)
            .accessDeniedHandler(authAccessDeniedHandler)
            .and()
            .authorizeRequests()
            .antMatchers("/api/user/register","/api/user/login").permitAll()
            .antMatchers("/api/**").hasAuthority("user")
            .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserService).passwordEncoder(passwordEncoder());
    }
}
