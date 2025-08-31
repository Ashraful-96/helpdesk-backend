package com.aust.its.controller;

import com.aust.its.config.security.CustomUserDetailsService;
import com.aust.its.dto.AuthenticationResponse;
import com.aust.its.dto.LoginPayload;
import com.aust.its.dto.RegisterPayload;
import com.aust.its.dto.token.JwtUsrInfo;
import com.aust.its.entity.HelpDeskRole;
import com.aust.its.entity.HelpDeskUser;
import com.aust.its.entity.User;
import com.aust.its.repository.UserRepository;
import com.aust.its.service.AuthenticationService;
import com.aust.its.service.HelpDeskRoleService;
import com.aust.its.service.HelpDeskUserService;
import com.aust.its.service.UserService;
import com.aust.its.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final HelpDeskUserService helpDeskUserService;
    private final HelpDeskRoleService helpDeskRoleService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

//    @PostMapping("/login")
//    public User login(@RequestBody LoginPayload loginPayload) {
//
//        logger.info("login payload is : {}", loginPayload);
//
//        User user = userRepository.findByUsernameAndPassword(loginPayload.username(), loginPayload.password())
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//
//        logger.info("Currently login user is : {}", user);
//        return user;
//    }

//    @PostMapping("/authenticate")
//    public String authenticate(@RequestBody LoginPayload loginPayload) {
//
//        logger.info("login payload is : {}", loginPayload);
//
//        User user = userService.getByUsernameAndPassword(loginPayload.username(), loginPayload.password());
//
//        JwtUsrInfo jwtUsrInfo;
//        if(loginPayload.isAdmin()) {
//            jwtUsrInfo = JwtUsrInfo.withAdminUsrId(user.getUsername(), user.getId(), user.getRole(), "admin: ".concat(String.valueOf(user.getId())));
//        } else {
//            jwtUsrInfo = JwtUsrInfo.of(user.getUsername(), user.getId(), user.getRole());
//        }
//
//        String jwt = authenticationService.generateToken(jwtUsrInfo);
//        logger.info("Jwt Token is : {}", jwt);
//
//        return jwt;
//    }

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

        final UserDetails userDetails = customUserDetailsService.loadUserByUsername(loginPayload.userId());
        final String jwt = JwtUtils.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt, null));
    }

    @PostMapping("/register")
    public HelpDeskUser register(@RequestBody RegisterPayload registerPayload) {
        logger.info("register payload is : {}", registerPayload);
        return userService.register(registerPayload);
    }
}