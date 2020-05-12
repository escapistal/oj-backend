package com.xc.oj.config;

import com.xc.oj.service.JWTUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled=true,jsr250Enabled=true)
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
        http.headers().frameOptions().disable();
        http
            .exceptionHandling()
            .authenticationEntryPoint(authEntryPoint)
            .accessDeniedHandler(authAccessDeniedHandler)
            .and()
            .authorizeRequests()
            .antMatchers("/api/user/register","/api/user/login","/swagger-ui.html").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/api/img/**").permitAll()
                .antMatchers("/img/**").permitAll()
                .antMatchers("/api/announcement/list","/api/announcement/{id}").permitAll()
                .antMatchers("/api/problem/list","/api/problem/{id}").permitAll()
                .antMatchers("/api/contest/list","/api/contest/{id}").permitAll()
                .antMatchers("/api/submission/list","/api/submission/{id}").permitAll()
//            .antMatchers("/api/admin/**").hasAuthority("admin")
//            .antMatchers("/api/**").hasAnyAuthority("user")
            .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(jwtUserService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/webjars/**");
        web.ignoring().antMatchers("/swagger-resources/**");
        web.ignoring().antMatchers("/img/**");
        web.ignoring().antMatchers("/api/img/**");
    }
}
