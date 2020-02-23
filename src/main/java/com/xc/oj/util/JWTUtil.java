package com.xc.oj.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.jwt")
public class JWTUtil {

    private static long expireTime;
    private static String secret;

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        JWTUtil.expireTime = expireTime;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        JWTUtil.secret = secret;
    }

    public static String create(Map<String,Object> claims){
        return Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(new Date(System.currentTimeMillis()+expireTime))
                    .signWith(SignatureAlgorithm.HS512, secret)
                    .compact();
    }

    public static Claims getClaims(String token){
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }


}
