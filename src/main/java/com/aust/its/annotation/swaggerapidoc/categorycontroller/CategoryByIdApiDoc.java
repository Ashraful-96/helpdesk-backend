package com.aust.its.annotation.swaggerapidoc.categorycontroller;

import com.aust.its.dto.CategoryDto;
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
        summary = "Get Category by ID",
        description = "Retrieve details of a specific category by its ID.",
        parameters = {
                @Parameter(name = "id", description = "Category ID", required = true, example = "1")
        },
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Category found",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CategoryDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Invalid credentials",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "404",
                        description = "Category not found",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content
                )
        }
)
public @interface CategoryByIdApiDoc {
}
