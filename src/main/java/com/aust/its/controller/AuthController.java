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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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


    @Operation(
            summary = "User Login",
            description = "Authenticate a user using userId and password, and return JWT tokens.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Login credentials",
                    content = @Content(
                            schema = @Schema(implementation = LoginPayload.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful authentication, returns access and refresh tokens",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid credentials",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginPayload loginPayload) {
        logger.info("login payload is : {}", loginPayload);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginPayload.userId(), loginPayload.password())
        );

        return authenticationResponse(loginPayload.userId());
    }

    @Operation(
            summary = "User Registration",
            description = "Register User for the first time in the HelpDesk System",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Registration Payload",
                    content = @Content(
                            schema = @Schema(implementation = RegisterPayload.class)
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully registered, returns registered userId and role",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RegisterResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "User already exists in the helpdesk system",
                            content = @Content
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody @Valid RegisterPayload registerPayload) {
        logger.info("registration payload is : {}", registerPayload);
        HelpDeskUser user = userService.register(registerPayload);
        return ResponseEntity.ok(new RegisterResponse(user.getUserId(), user.getRoleId()));
    }


//    @Operation(
//            summary = "Generate New JWT Token",
//            description = "Generate a new access and refresh token using the existing valid refresh token passed in the `Authorization` header.",
//            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
//                    required = true,
//                    description = "Refresh Token Payload",
//                    content = @Content(
//                            schema = @Schema(implementation = String.class)
//                    )
//            ),
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "New JWT tokens (access token and refresh token)",
//                            content = @Content(
//                                    mediaType = "application/json",
//                                    schema = @Schema(implementation = AuthenticationResponse.class)
//                            )
//                    ),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Invalid refresh token passed in the request",
//                            content = @Content
//                    ),
//                    @ApiResponse(
//                            responseCode = "401",
//                            description = "Missing or malformed Authorization header",
//                            content = @Content
//                    )
//            }
//    )

    @Operation(
            summary = "Generate New JWT Token",
            description = "Generate a new access and refresh token using the existing valid refresh token passed in the `Authorization` header.",
            parameters = {
                    @Parameter(
                            name = "Authorization",
                            description = "Bearer token with refresh token",
                            required = true,
                            in = ParameterIn.HEADER
//                            schema = @Schema(type = "string", format = "Bearer {token}")
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "New JWT tokens (access token and refresh token)",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid refresh token passed in the request",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Missing or malformed Authorization header",
                            content = @Content
                    )
            }
    )
    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if(Commons.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid Authorization header");
        }

        final String token = authorizationHeader.substring(7);

        if(!JwtUtils.validateToken(token)) {
            throw new BadCredentialsException("Invalid refresh token");
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