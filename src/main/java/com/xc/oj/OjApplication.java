package com.xc.oj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

@SpringBootApplication
public class OjApplication {

    public static void main(String[] args) {

        SpringApplication.run(OjApplication.class, args);
    }
}