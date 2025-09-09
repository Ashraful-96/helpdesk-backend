package com.aust.its.annotation.swaggerapidoc.cachecontroller;

import com.aust.its.dto.passwordtoken.TokenDataListView;
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
        summary = "Get All Active Tokens with TTL",
        description = "Retrieve a list of all active user tokens along with their remaining time-to-live (TTL) values.",
        responses = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Successfully retrieved list of tokens with TTL",
                        content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = TokenDataListView.class)
                        )
                ),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized access (if the user is not authenticated)",
                        content = @Content
                ),
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal server error",
                        content = @Content
                )
        }
)
public @interface CacheListApiDoc {
}
