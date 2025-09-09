package com.aust.its.annotation.swaggerapidoc.developercontroller;

import com.aust.its.dto.IssuesOfDeveloperDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        summary = "Get Issues of a Developer",
        description = "Retrieve all issues assigned to a specific developer by userId.",
        parameters = {
                @Parameter(
                        name = "id",
                        description = "User ID of the developer",
                        required = true,
                        example = "dev123"
                )
        },
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Developer's issues retrieved successfully",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = IssuesOfDeveloperDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid credentials",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Developer not found",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content
                )
        }
)
public @interface DeveloperIssueApiDoc {
}
