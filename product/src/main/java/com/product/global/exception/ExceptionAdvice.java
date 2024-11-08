package com.product.global.exception;

import com.product.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

    @InitBinder
    void initBinder(WebDataBinder binder) {
        binder.initDirectFieldAccess();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    ApiResponse<Object> bindException(BindException e) {
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
    ApiResponse<Object> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ApiResponse.of(
            HttpStatus.BAD_REQUEST,
            ErrorResponse.builder()
                .errorCode(1001)
                .errorMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage())
                .build()
        );
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomValidationException.class)
    ApiResponse<Object> customValidationException(CustomValidationException e) {
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
}
