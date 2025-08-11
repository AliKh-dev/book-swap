package com.alikh.bookswap.controller;

import com.alikh.bookswap.exception.parents.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentConversionNotSupportedException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.*;

@Slf4j
@RestControllerAdvice
public class ApiErrorHandler {

    // 400 - Bean validation on @RequestBody DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), Optional.ofNullable(fe.getDefaultMessage()).orElse("Invalid value"));
        }
        log.warn("400 VALIDATION_FAILED {} -> {}", uri(req), errors);
        return createProblemDetail(HttpStatus.BAD_REQUEST,
                "VALIDATION_FAILED",
                "Validation failed",
                "One or more fields are invalid.",
                req,
                errors);
    }

    // 400 - Bean validation on query/path params (@Validated)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (ConstraintViolation<?> v : ex.getConstraintViolations()) {
            errors.put(String.valueOf(v.getPropertyPath()),
                    Optional.ofNullable(v.getMessage()).orElse("Invalid value"));
        }
        log.warn("400 CONSTRAINT_VIOLATION {} -> {}", uri(req), errors);
        return createProblemDetail(HttpStatus.BAD_REQUEST,
                "CONSTRAINT_VIOLATION",
                "Constraint violation",
                "Request parameters are invalid.",
                req,
                errors);
    }

    // 400 - Binding failures (e.g., type mismatch for @ModelAttribute / form)
    @ExceptionHandler(BindException.class)
    public ProblemDetail handleBind(BindException ex, HttpServletRequest req) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getFieldErrors()) {
            errors.put(fe.getField(), Optional.ofNullable(fe.getDefaultMessage()).orElse("Invalid value"));
        }
        log.warn("400 BINDING_FAILED {} -> {}", uri(req), errors);
        return createProblemDetail(HttpStatus.BAD_REQUEST,
                "BINDING_FAILED",
                "Binding failed",
                "Could not bind request parameters.",
                req,
                errors);
    }

    // 400 - Malformed JSON or unreadable payload
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleNotReadable(HttpMessageNotReadableException ex, HttpServletRequest req) {
        log.warn("400 PAYLOAD_NOT_READABLE {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(HttpStatus.BAD_REQUEST,
                "PAYLOAD_NOT_READABLE",
                "Malformed or unreadable payload",
                "Request body is missing or malformed.",
                req,
                null);
    }

    // 405 - Wrong HTTP method
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ProblemDetail handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        log.warn("405 METHOD_NOT_ALLOWED {} -> {}", uri(req), ex.getMethod());
        return createProblemDetail(HttpStatus.METHOD_NOT_ALLOWED,
                "METHOD_NOT_ALLOWED",
                "Method not allowed",
                "Unsupported HTTP method for this endpoint.",
                req,
                null);
    }

    // 400 - Type mismatch for path/query parameter
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentConversionNotSupportedException.class})
    public ProblemDetail handleTypeMismatch(Exception ex, HttpServletRequest req) {
        log.warn("400 TYPE_MISMATCH {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(HttpStatus.BAD_REQUEST,
                "TYPE_MISMATCH",
                "Type mismatch",
                "Provided argument has an invalid type.",
                req,
                null);
    }

    // 401 - Authentication problems
    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handleAuthentication(AuthenticationException ex, HttpServletRequest req) {
        log.warn("401 AUTHENTICATION {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(ex.getStatus(), ex.getCode(), ex.getTitle(), ex.getMessage(), req, null);
    }

    // 403 - Authorization/permissions
    @ExceptionHandler(AuthorizationException.class)
    public ProblemDetail handleAuthorization(AuthorizationException ex, HttpServletRequest req) {
        log.warn("403 AUTHORIZATION {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(ex.getStatus(), ex.getCode(), ex.getTitle(), ex.getMessage(), req, null);
    }

    // 404 - Resource not found
    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex, HttpServletRequest req) {
        log.warn("404 NOT_FOUND {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(ex.getStatus(), ex.getCode(), ex.getTitle(), ex.getMessage(), req, null);
    }

    // 409 - State/data conflicts
    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleConflict(ConflictException ex, HttpServletRequest req) {
        log.warn("409 CONFLICT {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(ex.getStatus(), ex.getCode(), ex.getTitle(), ex.getMessage(), req, null);
    }

    // 422 - Business rule violations
    @ExceptionHandler(BusinessException.class)
    public ProblemDetail handleBusiness(BusinessException ex, HttpServletRequest req) {
        log.warn("422 BUSINESS {} -> {}", uri(req), safeMsg(ex));
        return createProblemDetail(ex.getStatus(), ex.getCode(), ex.getTitle(), ex.getMessage(), req, null);
    }


    /* ====================== Fallback ====================== */

    @ExceptionHandler(ApiException.class) // future parents / safety net
    public ProblemDetail handleApi(ApiException ex, HttpServletRequest req) {
        log.warn("{} API_EXCEPTION {} -> {}", ex.getStatus().value(), uri(req), safeMsg(ex));
        return createProblemDetail(ex.getStatus(), ex.getCode(), ex.getTitle(), ex.getMessage(), req, null);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnknown(Exception ex, HttpServletRequest req) {
        log.error("500 INTERNAL_ERROR {} -> {}", uri(req), safeMsg(ex), ex);
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_ERROR",
                "Internal server error",
                "Unexpected error, please contact support.",
                req,
                null);
    }


    /* ====================== helper ====================== */

    private ProblemDetail createProblemDetail(HttpStatus status, String code, String title, String detail,
                                              HttpServletRequest req, Map<String, ?> errors) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        if (title != null) problem.setTitle(title);
        if (detail != null) problem.setDetail(detail);
        if (req != null) problem.setInstance(URI.create(req.getRequestURI()));
        if (code != null) problem.setProperty("code", code);
        if (errors != null && !errors.isEmpty()) problem.setProperty("errors", errors);
        return problem;
    }

    private String uri(HttpServletRequest req) {
        return req != null ? req.getRequestURI() : "-";
    }

    private String safeMsg(Throwable ex) {
        String msg = ex.getMessage();
        if (msg == null || msg.isBlank()) return ex.getClass().getSimpleName();
        return msg.length() > 300 ? msg.substring(0, 300) + "…" : msg;
    }
}
