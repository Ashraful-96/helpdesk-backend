package com.aust.its.controller;

import com.aust.its.config.security.CustomUserDetailsService;
import com.aust.its.dto.AuthenticationResponse;
import com.aust.its.dto.LoginPayload;
import com.aust.its.dto.RegisterPayload;
import com.aust.its.dto.RegisterResponse;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.service.UserService;
import com.aust.its.utils.Const;
import com.aust.its.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;


    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody LoginPayload loginPayload) throws Exception {
        logger.info("login payload is : {}", loginPayload);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginPayload.userId(), loginPayload.password())
            );
        } catch(BadCredentialsException e) {
            throw new Exception("Incorrect Username or password" , e);
        }

        return authenticationResponse(loginPayload.userId());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterPayload registerPayload) {
        HelpDeskUser user = userService.register(registerPayload);
        return ResponseEntity.ok(new RegisterResponse(user.getUserId(), user.getRoleId()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            final String token = authorizationHeader.substring(7);

            if(!JwtUtils.validateToken(token)) {
                return ResponseEntity.badRequest().body("Invalid token");
            }

            String userId = JwtUtils.extractUsername(token);
            return authenticationResponse(userId);
        }
        return ResponseEntity.badRequest().body("Token missing or invalid format");
    }

    private ResponseEntity<?> authenticationResponse(String userId) {
        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(userId);
        final String accessToken = JwtUtils.generateToken(userDetails, Const.Jwt.ACCESS_TOKEN_EXPIRATION_MILISEC);
        final String refreshToken = JwtUtils.generateToken(userDetails, Const.Jwt.REFRESH_TOKEN_EXPIRATION_MILISEC);
        return ResponseEntity.ok(new AuthenticationResponse(accessToken, refreshToken));
    }
}