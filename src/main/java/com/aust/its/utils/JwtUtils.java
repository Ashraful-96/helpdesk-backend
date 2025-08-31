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

//    public static void validateToken(String token) {
//        try {
//            Jwts.parserBuilder()
//                    .setSigningKey(Const.Jwt.secretKey)
//                    .build()
//                    .parseClaimsJws(token);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static JwtUsrInfo extractJwtUserInfo(String token) {
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(Const.Jwt.secretKey)
//                    .build()
//                    .parseClaimsJws(token)
//                    .getBody();
//
//            String userInfoJson = claims.get("userInfo", String.class);
//            return objectMapper.readValue(userInfoJson, JwtUsrInfo.class);
//        } catch (Exception e) {
//            throw new RuntimeException("Invalid token", e);
//        }
//    }
//
////    public static String generateToken(JwtUsrInfo jwtUsrInfo) {
////        try {
////            ObjectMapper objectMapper = new ObjectMapper();
////            String userInfoJson = objectMapper.writeValueAsString(jwtUsrInfo);
////
////            return Jwts.builder()
////                    .setSubject(jwtUsrInfo.usrId())
////                    .claim("userInfo", userInfoJson)
////                    .setIssuedAt(new Date())
////                    .setExpiration(new Date(System.currentTimeMillis() + Const.Jwt.expirationMillis))
////                    .signWith(Const.Jwt.secretKey, SignatureAlgorithm.HS256)
////                    .compact();
////        } catch (Exception e) {
////            throw new RuntimeException("Error generating token", e);
////        }
////    }
//
//    public static String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private static Claims extractAllClaims(String token) {
//        return Jwts.parser().setSigningKey(Const.Jwt.secretKey).parseClaimsJws(token).getBody();
//    }
//
//    public static String generateToken(UserDetails userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, userDetails.getUsername());
//    }
//
//    private static String createToken(Map<String, Object> claims, String subject) {
//        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
//                .signWith(Const.Jwt.secretKey).compact();
//    }
//
//    public static Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    private static Boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    public static Boolean validateToken(String token, UserDetails userDetails) {
//        final String username = extractUsername(token);
//        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
//    }

    // ✅ Validate token syntax
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

    // ✅ Extract custom userInfo object
    public static JwtUsrInfo extractJwtUserInfo(String token) {
        try {
            Claims claims = extractAllClaims(token);
            String userInfoJson = claims.get("userInfo", String.class);
            return objectMapper.readValue(userInfoJson, JwtUsrInfo.class);
        } catch (Exception e) {
            throw new RuntimeException("Invalid token", e);
        }
    }

    // ✅ Generate token with custom payload
    public static String generateToken(JwtUsrInfo jwtUsrInfo) {
        try {
            String userInfoJson = objectMapper.writeValueAsString(jwtUsrInfo);

            return Jwts.builder()
                    .setSubject(jwtUsrInfo.usrId())
                    .claim("userInfo", userInfoJson)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + Const.Jwt.expirationMillis))
                    .signWith(Const.Jwt.secretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Error generating token", e);
        }
    }

    // ✅ Extract username (subject)
    public static String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ✅ Extract claims with resolver
    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ✅ Extract all claims
    private static Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Const.Jwt.secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Generate token from Spring Security UserDetails
    public static String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private static String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Const.Jwt.expirationMillis))
                .signWith(Const.Jwt.secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // ✅ Extract expiration
    public static Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ✅ Validate against UserDetails
    public static Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && validateToken(token));
    }

}
