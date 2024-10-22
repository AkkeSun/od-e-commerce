package com.sweettracker.account.global.util;

import com.sweettracker.account.account.domain.Account;

public interface JwtUtil {

    String createAccessToken(Account account);

    String createRefreshToken(String email);

    boolean validateTokenExceptExpiration(String token);

    String getEmail(String token);

}
