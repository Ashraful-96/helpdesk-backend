package com.aust.its.annotation.swaggerapidoc.authcontroller;

import com.aust.its.dto.RegisterPayload;
import com.aust.its.dto.RegisterResponse;
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
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content
                )
        }
)
public @interface RegisterApiDoc {
}
