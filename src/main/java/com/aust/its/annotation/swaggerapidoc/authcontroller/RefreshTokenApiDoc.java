package com.aust.its.annotation.swaggerapidoc.authcontroller;

import com.aust.its.dto.AuthenticationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
        summary = "Generate New JWT Token",
        description = "Generate a new access and refresh token using the existing valid refresh token passed in the `Authorization` header.",
        parameters = {
                @Parameter(
                        name = "X-Refresh-Token",
                        in = ParameterIn.HEADER,
                        description = "Bearer refresh token",
                        required = true,
                        example = "Bearer XXXXXXXXXXXXXXXX"
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
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content
                )
        }
)
public @interface RefreshTokenApiDoc {
}
