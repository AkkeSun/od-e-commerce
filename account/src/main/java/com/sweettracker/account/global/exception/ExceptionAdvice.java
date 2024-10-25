package com.sweettracker.account.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweettracker.account.global.aop.ExceptionHandlerLog;
import com.sweettracker.account.global.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    ApiResponse<Object> bindException(BindException e, HttpServletRequest request) {
        log.info("[{} {}] request - {}", request.getMethod(), request.getRequestURI(),
            getRequestData(request));
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            ErrorResponse.builder()
                .errorCode(1001)
                .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ApiResponse<Object> MethodArgumentNotValidException(MethodArgumentNotValidException e,
        HttpServletRequest request) {
        log.info("[{} {}] request - {}", request.getMethod(), request.getRequestURI(),
            getRequestData(request));
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            ErrorResponse.builder()
                .errorCode(1001)
                .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build()
        );
    }

    @ExceptionHandlerLog
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomValidationException.class)
    ApiResponse<Object> customValidationException(CustomValidationException e,
        HttpServletRequest request) {
        log.info("[{} {}] request - {}", request.getMethod(), request.getRequestURI(),
            getRequestData(request));
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            ErrorResponse.builder()
                .errorCode(1001)
                .errorMessage(e.getErrorMessage())
                .build()
        );
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomNotFoundException.class)
    ApiResponse<Object> notFoundException(CustomNotFoundException e) {
        return ApiResponse.of(
            HttpStatus.NOT_FOUND,
            ErrorResponse.builder()
                .errorCode(e.getErrorCode().getCode())
                .errorMessage(e.getErrorCode().getMessage())
                .build()
        );
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomAuthenticationException.class)
    ApiResponse<Object> customAuthenticationException(CustomAuthenticationException e) {
        return ApiResponse.of(
            HttpStatus.UNAUTHORIZED,
            ErrorResponse.builder()
                .errorCode(e.getErrorCode().getCode())
                .errorMessage(e.getErrorCode().getMessage())
                .build()
        );
    }

    @ExceptionHandlerLog
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(CustomBusinessException.class)
    ApiResponse<Object> notFoundException(CustomBusinessException e) {
        return ApiResponse.of(
            HttpStatus.UNPROCESSABLE_ENTITY,
            ErrorResponse.builder()
                .errorCode(e.getErrorCode().getCode())
                .errorMessage(e.getErrorCode().getMessage())
                .build()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    ApiResponse<Object> notFoundException(Exception e) {
        return ApiResponse.of(
            HttpStatus.INTERNAL_SERVER_ERROR,
            ErrorResponse.builder()
                .errorCode(500)
                .errorMessage(e.getMessage())
                .build()
        );
    }

    private Map<String, Object> getRequestData(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();

        // request parameter
        request.getParameterMap().forEach((key, value) -> {
            params.put(key, String.join(",", value));
        });

        // request body
        try {
            ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
            String requestBody = new String(cachingRequest.getContentAsByteArray(),
                StandardCharsets.UTF_8);
            params.putAll(new ObjectMapper().readValue(requestBody, Map.class));
        } catch (Exception ignored) {
        }

        return params;
    }
}
