package com.mamlayev.studentapi.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Uniform JSON error body.
 */
public record ApiError(
        Instant timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> details
) {
}
