package com.sweettracker.account.account.adapter.out.persistence.jpa;

import com.sweettracker.account.account.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "ACCOUNT")
@NoArgsConstructor
class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "USER_NAME")
    private String username;

    @Column(name = "USER_TEL")
    private String userTel;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "ROLE")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Column(name = "REG_DATE")
    private String regDate;

    @Builder
    AccountEntity(Long id, String email, String password, String username, String userTel,
        String address, Role role, LocalDateTime regDateTime, String regDate) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.username = username;
        this.userTel = userTel;
        this.address = address;
        this.role = role;
        this.regDateTime = regDateTime;
        this.regDate = regDate;
    }
}
