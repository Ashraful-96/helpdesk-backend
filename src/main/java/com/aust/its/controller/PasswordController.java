package com.aust.its.controller;

import com.aust.its.dto.ChangePasswordPayload;
import com.aust.its.dto.ForgetPasswordPayload;
import com.aust.its.dto.PasswordValidationTokenPayload;
import com.aust.its.service.PasswordService;
import com.aust.its.utils.Const;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);
    private final AuthenticationManager authenticationManager;
    private final PasswordService passwordService;

    @PostMapping("/forget")
    public ResponseEntity<?> forgetPassword(@RequestBody ForgetPasswordPayload forgetPasswordPayload) {
        return ResponseEntity.ok(passwordService.storeToken(forgetPasswordPayload.userId()));
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@RequestBody PasswordValidationTokenPayload passwordValidationTokenPayload) {
        boolean isValid = passwordService.isPasswordUpdateTokenValid(passwordValidationTokenPayload.userId(), passwordValidationTokenPayload.token());
        String tokenValidationMessage = isValid ? Const.token.VALID_TOKEN : Const.token.INVALID_TOKEN;
        return ResponseEntity.ok(tokenValidationMessage);
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
                throw new RuntimeException(Const.token.INVALID_TOKEN);
            }
        } else {
            userName = userDetails.getUsername();
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userName, changePasswordPayload.oldPassword())
        );

        return passwordService.updatePassword(userName, changePasswordPayload.newPassword());
    }
}
