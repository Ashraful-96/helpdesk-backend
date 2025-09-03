package com.aust.its.dto;

public record PasswordValidationTokenPayload(
    String userId,
    String token
) { }
