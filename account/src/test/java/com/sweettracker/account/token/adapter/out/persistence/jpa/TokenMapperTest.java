package com.sweettracker.account.token.adapter.out.persistence.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.token.domain.Token;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TokenMapperTest {

    TokenMapper mapper = new TokenMapper();

    @Nested
    @DisplayName("[toDomain] entity 를 domain 으로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[SUCCESS] entity 가 domain 으로 잘 변환되는지 확인한다.")
        void toDomain_success() {
            // given
            TokenEntity entity = TokenEntity.builder()
                .id(10L)
                .email("testEmail")
                .userAgent("testUserAgent")
                .refreshToken("test")
                .build();

            // when
            Token domain = mapper.toDomain(entity);

            // then
            assertThat(domain.getId()).isEqualTo(entity.getId());
            assertThat(domain.getEmail()).isEqualTo(entity.getEmail());
            assertThat(domain.getUserAgent()).isEqualTo(entity.getUserAgent());
            assertThat(domain.getRefreshToken()).isEqualTo(entity.getRefreshToken());
        }
    }

    @Nested
    @DisplayName("[toEntity] domain 을 entity 로 변환하는 메소드")
    class Describe_toEntity {

        @Test
        @DisplayName("[SUCCESS] domain 이 entity 로 잘 변환되는지 확인한다.")
        void toDomain_success() {
            // given
            Token domain = Token.builder()
                .id(10L)
                .email("testEmail")
                .userAgent("testUserAgent")
                .refreshToken("test")
                .build();

            // when
            TokenEntity entity = mapper.toEntity(domain);

            // then
            assertThat(entity.getId()).isEqualTo(domain.getId());
            assertThat(entity.getEmail()).isEqualTo(domain.getEmail());
            assertThat(entity.getUserAgent()).isEqualTo(domain.getUserAgent());
            assertThat(entity.getRefreshToken()).isEqualTo(domain.getRefreshToken());
        }
    }

}