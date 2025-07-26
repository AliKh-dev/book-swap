package com.alikh.bookswap.dto.error;

import java.util.Map;

public record ValidationErrorResponse(
        String error,
        String message,
        Map<String, String> fieldErrors
) {
}
