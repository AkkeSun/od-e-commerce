package com.sweettracker.account.account.adapter.in.register_token;

import com.sweettracker.account.account.application.port.in.RegisterTokenUseCase;
import com.sweettracker.account.account.application.service.register_token.RegisterTokenServiceResponse;
import com.sweettracker.account.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterTokenController {

    private final RegisterTokenUseCase registerTokenUseCase;

    @PostMapping("/accounts/token")
    ApiResponse<RegisterTokenResponse> registerToken(
        @RequestBody @Valid RegisterTokenRequest request) {
        RegisterTokenServiceResponse serviceResponse = registerTokenUseCase
            .registerToken(request.toCommand());
        
        return ApiResponse.ok(new RegisterTokenResponse().of(serviceResponse));
    }
}
