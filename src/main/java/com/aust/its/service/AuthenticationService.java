package com.aust.its.service;

import com.aust.its.dto.token.JwtUsrInfo;
import com.aust.its.utils.Const;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthenticationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateToken(JwtUsrInfo jwtUsrInfo) {
        try {
            String userInfoJson = objectMapper.writeValueAsString(jwtUsrInfo);

            return Jwts.builder()
                    .setSubject(jwtUsrInfo.username())
                    .claim("userInfo", userInfoJson)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + Const.Jwt.expirationMillis))
                    .signWith(Const.Jwt.secretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    public JwtUsrInfo extractJwtUserInfo(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Const.Jwt.secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String userInfoJson = claims.get("userInfo", String.class);
            return objectMapper.readValue(userInfoJson, JwtUsrInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }
}
