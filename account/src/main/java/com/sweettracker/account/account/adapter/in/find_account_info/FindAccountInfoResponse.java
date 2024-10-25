package com.sweettracker.account.account.adapter.in.find_account_info;

import com.sweettracker.account.account.application.service.find_account_info.FindAccountServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
class FindAccountInfoResponse {

    private Long id;
    private String email;
    private String username;
    private String userTel;
    private String address;
    private String role;
    private String regDate;

    @Builder
    public FindAccountInfoResponse(Long id, String email, String username, String userTel,
        String address, String role, String regDate) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
        this.regDate = regDate;
    }

    FindAccountInfoResponse of(FindAccountServiceResponse serviceResponse) {
        return FindAccountInfoResponse.builder()
            .id(serviceResponse.id())
            .email(serviceResponse.email())
            .username(serviceResponse.username())
            .userTel(serviceResponse.userTel())
            .address(serviceResponse.address())
            .regDate(serviceResponse.regDate())
            .role(serviceResponse.role())
            .build();
    }
}
