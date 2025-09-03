package com.aust.its.dto.passwordtoken;

public record TokenData(
        String randomUUID,
        long timeInMillis
) { }
