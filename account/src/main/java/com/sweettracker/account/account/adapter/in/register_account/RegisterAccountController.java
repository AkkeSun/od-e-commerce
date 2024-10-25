package com.sweettracker.account.account.adapter.in.register_account;

import com.sweettracker.account.account.application.port.in.RegisterAccountUseCase;
import com.sweettracker.account.account.application.service.register_account.RegisterAccountServiceResponse;
import com.sweettracker.account.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterAccountController {

    private final RegisterAccountUseCase registerAccountUseCase;

    @PostMapping("/accounts")
    ApiResponse<RegisterAccountResponse> registerAccount(
        @RequestBody @Valid RegisterAccountRequest request) {
        request.validation();

        final RegisterAccountServiceResponse serviceResponse = registerAccountUseCase
            .registerAccount(request.toCommand());
        return ApiResponse.ok(new RegisterAccountResponse().of(serviceResponse));
    }
}
