package com.sweettracker.account.account.adapter.in.delete_account;

import com.sweettracker.account.account.application.service.delete_account.DeleteAccountServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class DeleteAccountResponse {

    private Long id;
    private String email;
    private String result;

    @Builder
    DeleteAccountResponse(Long id, String email, String result) {
        this.id = id;
        this.email = email;
        this.result = result;
    }

    DeleteAccountResponse of(DeleteAccountServiceResponse serviceResponse){
        return DeleteAccountResponse.builder()
            .id(serviceResponse.id())
            .email(serviceResponse.email())
            .result(serviceResponse.result())
            .build();
    }
}
