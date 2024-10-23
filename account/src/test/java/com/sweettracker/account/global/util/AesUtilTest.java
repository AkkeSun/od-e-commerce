package com.sweettracker.account.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class AesUtilTest extends IntegrationTestSupport {

    @Autowired
    AesUtil aesUtil;

    @Nested
    @DisplayName("[encryptText] 문자열을 aes 암호화 하여 응답하는 메소드")
    class Describe_encryptText {

        @Test
        @DisplayName("[success] 정상적으로 문자열이 암호화되는지 확인한다.")
        void success() {
            // given
            String plainText = "plainText";

            // when
            String result = aesUtil.encryptText(plainText);

            // then
            assertThat(aesUtil.matches(plainText, result)).isTrue();
        }
    }

    @Nested
    @DisplayName("[decryptText] 문자열을 aes 복호화 하여 응답하는 메소드")
    class Describe_decryptText {

        @Test
        @DisplayName("[success] 정상적으로 문자열이 복호화되는지 확인한다.")
        void success() {
            // given
            String encryptedText = "7EC082C959F3010ED6FB40801B27039E";

            // when
            String result = aesUtil.decryptText(encryptedText);

            // then
            assertThat(result).isEqualTo("plainText");
        }
    }

    @Nested
    @DisplayName("[matches] 암호화된 문자열과 일반 문자열이 일치하는지 확인하는 메소드")
    class Describe_matches {

        @Test
        @DisplayName("[success] 암호화된 문자열과 일반 문자열이 일치한경우 true 를 응답한다.")
        void success() {
            // given
            String plainText = "plainText";
            String encryptedText = aesUtil.encryptText(plainText);

            // when
            boolean result = aesUtil.matches(plainText, encryptedText);

            // then
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("[success] 암호화된 문자열과 일반 문자열이 일치하지 않은 경우 false 를 응답한다.")
        void success2() {
            // given
            String plainText = "plainText";
            String encryptedText = "errorText";

            // when
            boolean result = aesUtil.matches(plainText, encryptedText);

            // then
            assertThat(result).isFalse();
        }
    }
}