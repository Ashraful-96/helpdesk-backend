package com.aust.its.controller;

import com.aust.its.dto.ChangePasswordPayload;
import com.aust.its.dto.ForgetPasswordPayload;
import com.aust.its.dto.PasswordValidationTokenPayload;
import com.aust.its.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final AuthenticationManager authenticationManager;
    private final PasswordService passwordService;

    @PostMapping("/forget")
    public String forgetPassword(@RequestBody ForgetPasswordPayload forgetPasswordPayload) {
        String randomNumber = UUID.randomUUID().toString().substring(0, 8);
        long timestamp = Instant.now().toEpochMilli();

        String data = randomNumber + ":" + timestamp;
        String encoded = Base64.getEncoder().encodeToString(data.getBytes());

        passwordService.storeToken(forgetPasswordPayload.userId(), randomNumber);
        return encoded;
    }

    @PostMapping("/validate-token")
    public String validateToken(@RequestBody PasswordValidationTokenPayload passwordValidationTokenPayload) {
        boolean isValid = passwordService.isPasswordUpdateTokenValid(passwordValidationTokenPayload.userId(), passwordValidationTokenPayload.token());
        if(isValid) {
            return "Valid Token !!";
        }
        return "Invalid Token !!";
    }

    @PostMapping("/change")
    public String changePassword(@RequestBody @Valid ChangePasswordPayload changePasswordPayload, @AuthenticationPrincipal UserDetails userDetails) {
        String userName = null;

        if(userDetails == null) {
            logger.info("this is an unauthenticated user");
            if(changePasswordPayload.token() == null) {
                throw new RuntimeException("token is null");
            }
            userName = changePasswordPayload.userId();
            boolean isValid = passwordService.isPasswordUpdateTokenValid(userName, changePasswordPayload.token());

            if(!isValid) {
                throw new RuntimeException("Invalid Token !!");
            }
        } else {
            userName = userDetails.getUsername();
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userName, changePasswordPayload.oldPassword())
            );
        } catch(BadCredentialsException e) {
            throw new RuntimeException("Incorrect Username or password" , e);
        }

        return passwordService.updatePassword(userName, changePasswordPayload.newPassword());
    }
}
