package com.aust.its.dto.passwordtoken;

public record TokenDataView(
        String uuid,
        long ttl
) { }
