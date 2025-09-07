package com.aust.its.service;

import com.aust.its.dto.passwordtoken.TokenData;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.repository.HelpDeskUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordService {

    private static final Logger logger = LoggerFactory.getLogger(PasswordService.class);
    private final HelpDeskUserRepository helpDeskUserRepository;
    private final CacheService cacheService;
    private final PasswordEncoder passwordEncoder;

    public PasswordService(HelpDeskUserRepository helpDeskUserRepository,
                           CacheService cacheService,
                           @Qualifier("customPasswordEncoderBean") PasswordEncoder passwordEncoder) {
        this.helpDeskUserRepository = helpDeskUserRepository;
        this.cacheService = cacheService;
        this.passwordEncoder = passwordEncoder;
    }

    public String updatePassword(String userId, String newPassword) {
        try {
            Optional<HelpDeskUser> helpDeskUserOptional = helpDeskUserRepository.findHelpDeskUserByUserId(userId);
            if(helpDeskUserOptional.isPresent()) {
                HelpDeskUser helpDeskUser = helpDeskUserOptional.get();
                helpDeskUser.setPassword(passwordEncoder.encode(newPassword));
                helpDeskUserRepository.save(helpDeskUser);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "Password Updated Successfully";
    }

    public boolean isPasswordUpdateTokenValid(String userId, String token) {
        try {
            String decoded = new String(Base64.getDecoder().decode(token));
            String[] parts = decoded.split(":");
            if (parts.length != 2) {
                return false;
            }

            String randomNumber = parts[0];
            long timestamp = Long.parseLong(parts[1]);

            long now = Instant.now().toEpochMilli();
            if (now - timestamp > 60000) {
                return false;
            }

            TokenData tokenData = cacheService.getToken(userId);
            return tokenData != null && tokenData.randomUUID().equalsIgnoreCase(randomNumber);
        } catch (Exception e) {
            return false;
        }
    }

    public String storeToken(String userId) {
        String randomNumber = UUID.randomUUID().toString().substring(0, 8);
        long timestamp = Instant.now().toEpochMilli();

        String data = randomNumber + ":" + timestamp;
        String encoded = Base64.getEncoder().encodeToString(data.getBytes());
        cacheService.storeToken(userId, randomNumber);
        return encoded;
    }
}
