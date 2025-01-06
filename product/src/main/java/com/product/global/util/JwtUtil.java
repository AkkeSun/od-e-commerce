package com.product.global.util;

import io.jsonwebtoken.Claims;

public interface JwtUtil {

    boolean validateTokenExceptExpiration(String token);

    Claims getClaims(String token);

    Long getAccountId(String token);
}
