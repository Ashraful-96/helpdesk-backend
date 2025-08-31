package com.aust.its.dto;

public record AuthenticationResponse(
    String accessToken,
    String refreshToken
) { }
