package com.aust.its.annotation.swaggerapidoc.authcontroller;

import com.aust.its.dto.AuthenticationResponse;
import com.aust.its.dto.LoginPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
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
public @interface LoginApiDoc {
}
