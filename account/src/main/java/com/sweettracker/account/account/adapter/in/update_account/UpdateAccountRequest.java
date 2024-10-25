package com.sweettracker.account.account.adapter.in.update_account;

import com.sweettracker.account.account.application.port.in.command.UpdateAccountCommand;
import com.sweettracker.account.global.exception.CustomValidationException;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@NoArgsConstructor
class UpdateAccountRequest {

    private String password;

    private String passwordCheck;

    private String username;

    private String userTel;

    private String address;

    @Builder
    UpdateAccountRequest(String password, String passwordCheck, String username,
        String userTel, String address) {
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
    }

    void validation() {
        if (StringUtils.hasText(password) && StringUtils.hasText(passwordCheck) &&
            !password.equals(passwordCheck)) {
            throw new CustomValidationException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if (StringUtils.hasText(userTel) &&
            !Pattern.compile("^01[016789]\\d{7,8}$").matcher(userTel).matches()) {
            throw new CustomValidationException("올바른 전화번호 형식이 아닙니다.");
        }
    }

    UpdateAccountCommand toCommand(String accessToken) {
        return UpdateAccountCommand.builder()
            .accessToken(accessToken)
            .password(password)
            .passwordCheck(passwordCheck)
            .username(username)
            .userTel(userTel)
            .address(address)
            .build();
    }
}
