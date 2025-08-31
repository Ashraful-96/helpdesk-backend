package com.aust.its.utils;

import com.aust.its.dto.token.JwtUsrInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static boolean validateToken(String token) {
        try {
            if(isTokenExpired(token)) {
                return false;
            }

            Jwts.parserBuilder()
                    .setSigningKey(Const.Jwt.secretKey)
                    .build()
                    .parseClaimsJws(token);

            return true;
        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public static JwtUsrInfo extractJwtUserInfo(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String userInfoJson = claims.get("userInfo", String.class);
            return objectMapper.readValue(userInfoJson, JwtUsrInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Const.Jwt.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public static String generateToken(UserDetails userDetails, long expirationMilliseconds) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMilliseconds))
                .signWith(Const.Jwt.secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && validateToken(token));
    }
}
