package com.aust.its.annotation.swaggerapidoc.passwordcontroller;

import com.aust.its.dto.PasswordValidationTokenPayload;
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
        summary = "Validate Password Reset Token",
        description = "Validate a password reset token for a given userId.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "User ID and reset token",
                content = @Content(schema = @Schema(implementation = PasswordValidationTokenPayload.class))
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Returns token validation status",
                        content = @Content(mediaType = "application/json")
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request payload",
                        content = @Content
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
public @interface ValidateTokenApiDoc {
}
