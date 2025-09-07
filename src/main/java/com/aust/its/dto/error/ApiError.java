package com.aust.its.dto.error;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError (
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> errors
) { }
