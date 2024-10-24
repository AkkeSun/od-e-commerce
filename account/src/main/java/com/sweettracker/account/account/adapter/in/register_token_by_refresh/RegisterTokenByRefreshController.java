package com.sweettracker.account.account.adapter.in.register_token_by_refresh;

import com.sweettracker.account.account.application.port.in.RegisterTokenByRefreshUseCase;
import com.sweettracker.account.account.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import com.sweettracker.account.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterTokenByRefreshController {

    private final RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase;

    @PostMapping("/accounts/refresh-token")
    ApiResponse<RegisterTokenByRefreshResponse> registerToken(
        @RequestBody @Valid RegisterTokenByRefreshRequest request) {
        RegisterTokenByRefreshServiceResponse serviceResponse = registerTokenByRefreshUseCase
            .registerTokenByRefresh(request.getRefreshToken());

        return ApiResponse.ok(new RegisterTokenByRefreshResponse().of(serviceResponse));
    }
}
