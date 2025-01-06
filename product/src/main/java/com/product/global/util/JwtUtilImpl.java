package com.product.global.util;

import com.product.global.exception.CustomAuthenticationException;
import com.product.global.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtilImpl implements JwtUtil {

    @Value("${jwt.token.secret-key}")
    private String secretKey;

    @Override
    public boolean validateTokenExceptExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return !claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Claims getClaims(String token) {
        try {
            token = token.replace("Bearer ", "");
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            throw new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
    }

    @Override
    public Long getAccountId(String token) {
        Claims claims = getClaims(token);
        return claims.get("accountId", Integer.class).longValue();
    }
}
