package com.sweettracker.account.account.domain;

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

    @Builder
    public Account(Long id, String email, String password, String username, String userTel,
        String address, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
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
}
