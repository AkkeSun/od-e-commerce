package com.sweettracker.account.account.adapter.in.find_account_info;

import com.sweettracker.account.account.application.port.in.FindAccountInfoUseCase;
import com.sweettracker.account.account.application.service.find_account_info.FindAccountServiceResponse;
import com.sweettracker.account.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
class FindAccountInfoController {

    private final FindAccountInfoUseCase findAccountInfoUseCase;

    @GetMapping("/accounts")
    ApiResponse<FindAccountInfoResponse> findAccountInfo(
        @RequestHeader(name = "Authorization", required = false) String authorization) {
        FindAccountServiceResponse serviceResponse = findAccountInfoUseCase.
            findAccountInfo(authorization);

        return ApiResponse.ok(new FindAccountInfoResponse().of(serviceResponse));
    }
}
