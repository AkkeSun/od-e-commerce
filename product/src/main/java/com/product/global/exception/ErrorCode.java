package com.product.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // status code 404 (2001 - 2099) : Not found error
    DoesNotExist_ACCOUNT_INFO(2001, "조회된 사용자 정보가 없습니다"),
    DoesNotExist_PROUCT_INFO(2002, "조회된 싱픔 정보가 없습니다"),

    // status code 401 (3001 - 3099) : Unauthorized
    INVALID_ACCESS_TOKEN(3001, "유효한 인증 토큰이 아닙니다"),
    INVALID_REFRESH_TOKEN(3002, "유효한 리프레시 토큰이 아닙니다"),
    INVALID_ACCESS_TOKEN_BY_SECURITY(3003, "유효한 인증 토큰이 아닙니다"),

    // status code 422 (4001 - 4099)
    Business_SAVED_ACCOUNT_INFO(4001, "등록된 사용자 정보 입니다"),
    Business_OUT_OF_STOCK(4002, "상품 재고가 부족합니다"),
    Business_ES_PRODUCT_SAVE(4003, "엘라스틱서치 상품 등록중 오류가 발생했습니다"),

    // status code 403 (5001 - 5099)
    ACCESS_DENIED(5001, "접근권한이 없습니다"),
    ACCESS_DENIED_BY_SECURITY(5002, "접근권한이 없습니다"),

    ;


    private final int code;
    private final String message;
}