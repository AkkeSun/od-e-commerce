package com.sweettracker.account.account.adapter.in.update_account;

import com.sweettracker.account.account.application.port.in.UpdateAccountUseCase;
import com.sweettracker.account.account.application.service.update_account.UpdateAccountServiceResponse;
import com.sweettracker.account.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class UpdateAccountController {

    private final UpdateAccountUseCase updateAccountUseCase;

    @PutMapping("/accounts")
    ApiResponse<UpdateAccountResponse> updateAccount(
        @RequestBody @Valid UpdateAccountRequest request,
        @RequestHeader(name = "Authorization", required = false) String accessToken) {
        request.validation();

        UpdateAccountServiceResponse serviceResponse = updateAccountUseCase
            .updateAccount(request.toCommand(accessToken));
        return ApiResponse.ok(new UpdateAccountResponse().of(serviceResponse));
    }
}
