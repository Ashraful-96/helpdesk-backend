package com.aust.its.dto;

import com.aust.its.annotation.EmailValid;
import jakarta.validation.constraints.NotBlank;

public record RegisterPayload(
    @NotBlank String userId,
    @NotBlank @EmailValid String email,
    @NotBlank String password
) { }
