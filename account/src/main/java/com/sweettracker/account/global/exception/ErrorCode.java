package com.sweettracker.account.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // status code 404 (2001 - 2099) : Not found error
    DoesNotExist_USER_INFO(2001, "조회된 사용자 정보가 없습니다"),

    // status code 401 (3001 - 3099) : Unauthorized
    INVALID_ACCESS_TOKEN(3001, "유효한 인증 토큰이 아닙니다"),
    INVALID_REFRESH_TOKEN(3002, "유효한 리프레시 토큰이 아닙니다"),

    ;

    private final int code;
    private final String message;
}