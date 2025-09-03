package com.aust.its.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordPayload(
        @NotBlank String userId,
        String token,
        @NotBlank String oldPassword,
        @NotBlank String newPassword
) { }
