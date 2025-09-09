package com.aust.its.annotation.swaggerapidoc.passwordcontroller;

import com.aust.its.dto.ChangePasswordPayload;
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
        summary = "Change Password",
        description = "Change password for an authenticated or unauthenticated user using a valid token.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "Password change payload containing old/new passwords and token if unauthenticated",
                content = @Content(schema = @Schema(implementation = ChangePasswordPayload.class))
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Password changed successfully",
                        content = @Content(mediaType = "application/json")
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid request or invalid token",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Authentication failed (wrong old password)",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content
                )
        }
)
public @interface ChangePasswordApiDoc {
}
