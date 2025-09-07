package com.aust.its.controller;

import com.aust.its.config.security.CustomUserDetailsService;
import com.aust.its.dto.AuthenticationResponse;
import com.aust.its.dto.LoginPayload;
import com.aust.its.dto.RegisterPayload;
import com.aust.its.dto.RegisterResponse;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.service.UserService;
import com.aust.its.utils.Commons;
import com.aust.its.utils.Const;
import com.aust.its.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginPayload loginPayload) throws BadCredentialsException {
        logger.info("login payload is : {}", loginPayload);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginPayload.userId(), loginPayload.password())
        );

        return authenticationResponse(loginPayload.userId());
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterPayload registerPayload) {
        logger.info("registration payload is : {}", registerPayload);
        HelpDeskUser user = userService.register(registerPayload);
        return ResponseEntity.ok(new RegisterResponse(user.getUserId(), user.getRoleId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if(Commons.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid Authorization header");
        }

        final String token = authorizationHeader.substring(7);

        if(!JwtUtils.validateToken(token)) {
            throw new BadCredentialsException("Invalid JWT token");
        }

        String userId = JwtUtils.extractUsername(token);
        return authenticationResponse(userId);
    }

    private ResponseEntity<AuthenticationResponse> authenticationResponse(String userId) {
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        final String accessToken = JwtUtils.generateToken(userDetails, Const.Jwt.ACCESS_TOKEN_EXPIRATION_MILISEC);
        final String refreshToken = JwtUtils.generateToken(userDetails, Const.Jwt.REFRESH_TOKEN_EXPIRATION_MILISEC);
        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
    }
}