package com.sweettracker.account.account.domain;

import com.sweettracker.account.token.domain.Token;
import io.jsonwebtoken.Claims;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Account {

    private Long id;

    private String email;

    private String password;

    private String username;

    private String userTel;

    private String address;

    private Role role;

    private LocalDateTime regDateTime;

    private String regDate;

    @Builder
    public Account(Long id, String email, String password, String username, String userTel,
        String address, Role role, LocalDateTime regDateTime, String regDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
        this.regDateTime = regDateTime;
        this.regDate = regDate;
    }

    public Account of(Token tokenCache) {
        return Account.builder()
            .id(tokenCache.getId())
            .email(tokenCache.getEmail())
            .username("")
            .userTel("")
            .address("")
            .role(Role.valueOf(tokenCache.getRole()))
            .build();
    }

    public Account of(Claims claims) {
        return Account.builder()
            .id(Long.valueOf((Integer) claims.get("accountId")))
            .email(claims.getSubject())
            .username("")
            .userTel("")
            .address("")
            .role(Role.valueOf((String) claims.get("role")))
            .build();
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUsername(String username) {
        this.username = username;
    }

    public void updateUserTel(String userTel) {
        this.userTel = userTel;
    }

    public void updateAddress(String address) {
        this.address = address;
    }
}
