package com.sweettracker.account.token.adapter.in.register_token;

import com.sweettracker.account.global.response.ApiResponse;
import com.sweettracker.account.token.application.port.in.RegisterTokenUseCase;
import com.sweettracker.account.token.application.service.register_token.RegisterTokenServiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterTokenController {

    private final RegisterTokenUseCase registerTokenUseCase;

    @PostMapping("/auth/login")
    ApiResponse<RegisterTokenResponse> registerToken(
        @RequestBody @Valid RegisterTokenRequest request) {
        RegisterTokenServiceResponse serviceResponse = registerTokenUseCase
            .registerToken(request.toCommand());
        
        return ApiResponse.ok(new RegisterTokenResponse().of(serviceResponse));
    }
}
