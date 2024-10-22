package com.sweettracker.account.account.adapter.in.find_account_info;

import com.sweettracker.account.account.application.service.find_account_info.FindAccountServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class FindAccountResponse {

    private Long id;
    private String email;
    private String username;
    private String userTel;
    private String address;
    private String role;

    @Builder
    public FindAccountResponse(Long id, String email, String username, String userTel,
        String address,
        String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
    }

    FindAccountResponse of(FindAccountServiceResponse serviceResponse) {
        return FindAccountResponse.builder()
            .id(serviceResponse.id())
            .email(serviceResponse.email())
            .username(serviceResponse.username())
            .userTel(serviceResponse.userTel())
            .address(serviceResponse.address())
            .role(serviceResponse.role())
            .build();
    }
}
