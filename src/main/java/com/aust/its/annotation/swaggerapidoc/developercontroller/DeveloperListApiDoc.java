package com.aust.its.annotation.swaggerapidoc.developercontroller;

import com.aust.its.entity.Developer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
        summary = "Get All Developers",
        description = "Retrieve a list of all developers.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "List of developers retrieved successfully",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = Developer.class))
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
public @interface DeveloperListApiDoc {
}
