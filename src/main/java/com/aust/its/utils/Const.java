package com.aust.its.utils;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class Const {

    public static final String EDU_WEB_BEARER_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4MzIwMDQiLCJleHAiOjE2NzU3NjUzMjV9.e7auXVVHk8crG0_O5JZItOxzaPipyvVlnqEdOAfKR1c";
    public static final String ROLE_AUST_EDU = "ROLE_AUST_EDU";

    public static final String[] WHITE_LIST = {
            "/api/v1/auth/**",
            "/v2/api-docs",
            "/api/auth/**",
            "/api/test/**",
            "/authenticate",
            "/api/authenticate",
            "/api/register",


            // Swagger & Actuator
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/webjars/**",
            "/swagger/**",
            "/actuator/**"
    };


    public static class Jwt {
        public static final String SECRET = "secretsdfsdsdvsdgsdvsdgsecretsdfsdsdvsdgsdvsdg";

        public static final SecretKey secretKey = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
//        public static final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        public static final long expirationMillis = 1000 * 60 * 60; // 1 hour
    }
}
