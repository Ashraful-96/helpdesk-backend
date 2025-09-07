package com.aust.its.dto.passwordtoken;

public record TokenDataView(
        String userId,
        String uuid,
        long ttl
) { }
