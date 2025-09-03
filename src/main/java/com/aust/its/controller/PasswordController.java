package com.aust.its.controller;

import com.aust.its.dto.ChangePasswordPayload;
import com.aust.its.dto.ForgetPasswordPayload;
import com.aust.its.dto.PasswordValidationTokenPayload;
import com.aust.its.dto.passwordtoken.TokenData;
import com.aust.its.service.CacheService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);
    private final CacheService cacheService;

    @PostMapping("/forget")
    public String forgetPassword(@RequestBody ForgetPasswordPayload forgetPasswordPayload) {
        String randomNumber = UUID.randomUUID().toString().substring(0, 8);
        long timestamp = Instant.now().toEpochMilli();

        String data = randomNumber + ":" + timestamp;
        String encoded = Base64.getEncoder().encodeToString(data.getBytes());

        cacheService.storeToken(forgetPasswordPayload.userId(), randomNumber);
        return encoded;
    }

    @PostMapping("/validate-token")
    public String validateToken(@RequestBody PasswordValidationTokenPayload passwordValidationTokenPayload) {
        try {
            String decoded = new String(Base64.getDecoder().decode(passwordValidationTokenPayload.token()));
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

            TokenData tokenData = cacheService.getToken(passwordValidationTokenPayload.userId());
            if (tokenData != null && tokenData.randomUUID().equalsIgnoreCase(randomNumber)) {
                return "Token valid";
            } else {
                return "Invalid token for user";
            }
        } catch (Exception e) {
            return "Invalid token";
        }
    }

    @PostMapping("/change")
    public String changePassword(@RequestBody @Valid ChangePasswordPayload changePasswordPayload, @AuthenticationPrincipal UserDetails userDetails) {
        if(userDetails == null) {
            logger.info("this is an unauthenticated user");
            if(changePasswordPayload.token() == null) {
                throw new RuntimeException("token is null");
            }
        }


        return "";
    }
}
