package com.sweettracker.account.global.config;

import com.sweettracker.account.account.application.port.out.FindAccountPort;
import com.sweettracker.account.account.application.port.out.RegisterAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.account.domain.Role;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import com.sweettracker.account.global.util.AesUtil;
import com.sweettracker.account.global.util.DateUtil;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitConfig {

    private final FindAccountPort findAccountPort;
    private final RegisterAccountPort registerAccountPort;
    private final AesUtil aesUtil;
    private final DateUtil dateUtil;

    @PostConstruct
    public void init() {
        Account account = Account.builder()
            .email("od@sweettracker.co.kr")
            .username("od")
            .address("서울특별시 송파구")
            .userTel("01012341234")
            .password(aesUtil.encryptText("1234"))
            .regDate(dateUtil.getCurrentDate())
            .regDateTime(LocalDateTime.now())
            .role(Role.ROLE_CUSTOMER)
            .build();

        try {
            findAccountPort.findByEmailAndPassword(account.getEmail(), account.getPassword());
        } catch (CustomNotFoundException e) {
            registerAccountPort.register(account);
        }
    }
}
