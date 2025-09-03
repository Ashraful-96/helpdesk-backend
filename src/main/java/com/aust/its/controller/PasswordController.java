package com.aust.its.controller;

import com.aust.its.dto.passwordtoken.TokenData;
import com.aust.its.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final CacheService cacheService;

    @PostMapping("/forget")
    public String forgetPassword(@RequestParam String userId) {
        String randomNumber = UUID.randomUUID().toString().substring(0, 8);
        long timestamp = Instant.now().toEpochMilli();

        String data = randomNumber + ":" + timestamp;
        String encoded = Base64.getEncoder().encodeToString(data.getBytes());

        cacheService.storeToken(userId, randomNumber);
        return encoded;
    }

    @PostMapping("/validate-token")
    public String validateToken(@RequestParam String userId, @RequestParam String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length != 2) {
                return "Invalid token format";
            }

            String randomNumber = parts[0];
            long timestamp = Long.parseLong(parts[1]);

            long now = Instant.now().toEpochMilli();
            if (now - timestamp > 60000) {
                return "Token expired";
            }

            TokenData tokenData = cacheService.getToken(userId);
            if (tokenData != null && tokenData.randomUUID().equalsIgnoreCase(randomNumber)) {
                return "Token valid";
            } else {
                return "Invalid token for user";
            }
        } catch (Exception e) {
            return "Invalid token";
        }
    }
}
