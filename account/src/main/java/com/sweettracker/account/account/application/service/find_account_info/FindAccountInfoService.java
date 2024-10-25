package com.sweettracker.account.account.application.service.find_account_info;

import com.sweettracker.account.account.application.port.in.FindAccountInfoUseCase;
import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.global.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class FindAccountInfoService implements FindAccountInfoUseCase {

    private final JwtUtil jwtUtil;
    private final FindAccountPort findAccountPort;

    @Override
    public FindAccountServiceResponse findAccountInfo(String authorization) {
        String email = jwtUtil.getEmail(authorization);
        Account account = findAccountPort.findByEmail(email);

        return FindAccountServiceResponse.builder()
            .id(account.getId())
            .email(account.getEmail())
            .username(account.getUsername())
            .userTel(account.getUserTel())
            .address(account.getAddress())
            .role(account.getRole().toString())
            .regDate(account.getRegDate())
            .build();
    }
}
