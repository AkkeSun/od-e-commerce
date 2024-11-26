package com.product_agent.global.exception;

import lombok.Getter;

@Getter
public class CustomNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public CustomNotFoundException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
