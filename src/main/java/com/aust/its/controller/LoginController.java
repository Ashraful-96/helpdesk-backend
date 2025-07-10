package com.aust.its.controller;

import com.aust.its.dto.LoginPayload;
import com.aust.its.dto.token.JwtUsrInfo;
import com.aust.its.entity.User;
import com.aust.its.repository.UserRepository;
import com.aust.its.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public User login(@RequestBody LoginPayload loginPayload) {

        logger.info("login payload is : {}", loginPayload);

        User user = userRepository.findByUsernameAndPassword(loginPayload.username(), loginPayload.password())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        logger.info("Currently login user is : {}", user);
        return user;
    }

    @PostMapping("/authenticate")
    public String authenticate(@RequestBody LoginPayload loginPayload) {

        logger.info("login payload is : {}", loginPayload);

        User user = userRepository.findByUsernameAndPassword(loginPayload.username(), loginPayload.password())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        JwtUsrInfo jwtUsrInfo;
        if(loginPayload.isAdmin()) {
            jwtUsrInfo = JwtUsrInfo.withAdminUsrId(user.getUsername(), user.getId(), user.getRole(), "admin: ".concat(String.valueOf(user.getId())));
        } else {
            jwtUsrInfo = JwtUsrInfo.of(user.getUsername(), user.getId(), user.getRole());
        }

        String jwt = authenticationService.generateToken(jwtUsrInfo);
        logger.info("Jwt Token is : {}", jwt);

        return jwt;
    }
}