package com.aust.its.annotation.swaggerapidoc.categorycontroller;

import com.aust.its.dto.CategoryDto;
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
        summary = "Create a New Category",
        description = "Create and save a new category.",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                required = true,
                description = "Category details",
                content = @Content(schema = @Schema(implementation = CategoryDto.class))
        ),
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Category created successfully",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = CategoryDto.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "400",
                        description = "Invalid input data",
                        content = @Content
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
public @interface CategoryCreateApiDoc {
}
