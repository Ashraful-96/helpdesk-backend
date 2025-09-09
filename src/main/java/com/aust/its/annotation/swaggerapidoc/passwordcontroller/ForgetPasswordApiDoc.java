package com.aust.its.annotation.swaggerapidoc.passwordcontroller;

import com.aust.its.dto.ForgetPasswordPayload;
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
        summary = "Request Password Reset",
        description = "Generate a password reset token for a given userId and store it.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "User ID for password reset",
                content = @Content(schema = @Schema(implementation = ForgetPasswordPayload.class))
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Password reset token generated successfully",
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
public @interface ForgetPasswordApiDoc {
}
