package com.alikh.bookswap.exception.parents;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ApiException extends RuntimeException {

    private final String code;
    private final HttpStatus status;
    private final String title;

    protected ApiException(String code, HttpStatus status, String title, String message) {
        super(message);
        this.code = code;
        this.status = status;
        this.title = title;
    }
}
