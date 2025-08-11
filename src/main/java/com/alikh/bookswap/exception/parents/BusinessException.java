package com.alikh.bookswap.exception.parents;

import org.springframework.http.HttpStatus;

public abstract class BusinessException extends ApiException {

    protected BusinessException(String code, String message) {
        super(code, HttpStatus.UNPROCESSABLE_ENTITY, "Business rule violation", message);
    }
}