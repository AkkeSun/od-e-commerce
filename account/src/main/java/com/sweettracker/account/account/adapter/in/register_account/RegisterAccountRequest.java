package com.sweettracker.account.account.adapter.in.register_account;

import com.sweettracker.account.account.application.port.in.command.RegisterAccountCommand;
import com.sweettracker.account.account.domain.Role;
import com.sweettracker.account.global.exception.CustomValidationException;
import jakarta.validation.constraints.NotBlank;
import java.util.regex.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.StringUtils;

@Getter
@ToString
@NoArgsConstructor
class RegisterAccountRequest {

    @NotBlank(message = "이메일은 필수값 입니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수값 입니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수값 입니다.")
    private String passwordCheck;

    @NotBlank(message = "권한은 필수값 입니다.")
    private String role;

    private String username;

    private String userTel;

    private String address;

    @Builder
    RegisterAccountRequest(String email, String password, String passwordCheck,
        String username,
        String userTel, String address, String role) {
        this.email = email;
        this.password = password;
        this.passwordCheck = passwordCheck;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
    }

    void validation() {
        if (!password.equals(passwordCheck)) {
            throw new CustomValidationException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        if (!role.equals(Role.ROLE_CUSTOMER.name()) && !role.equals(Role.ROLE_SELLER.name())) {
            throw new CustomValidationException("유효하지 않은 권한 입니다.");
        }
        if (StringUtils.hasText(userTel) &&
            !Pattern.compile("^01[016789]\\d{7,8}$").matcher(userTel).matches()) {
            throw new CustomValidationException("올바른 전화번호 형식이 아닙니다.");
        }
    }

    RegisterAccountCommand toCommand() {
        return RegisterAccountCommand.builder()
            .email(email)
            .password(password)
            .username(username)
            .userTel(userTel)
            .address(address)
            .role(role)
            .build();
    }
}
