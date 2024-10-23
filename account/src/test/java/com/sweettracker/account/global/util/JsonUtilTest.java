package com.sweettracker.account.global.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.account.domain.Account;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class JsonUtilTest {

    JsonUtilImpl jsonUtil = new JsonUtilImpl();

    @Nested
    @DisplayName("[parseJson] json 문자열을 객체로 변환하는 메소드")
    class Describe_parseJson {

        @Test
        @DisplayName("[success] 정상적으로 json 문자열이 객체로 변환되는지 확인한다.")
        void success() {
            // given
            String jsonText = "{\"id\":1,\"email\":\"od@gmail.com\",\"password\":\"1234\",\"username\":\"od\",\"userTel\":\"010-1234-5678\",\"address\":\"서울시 강남구\",\"role\":null}";

            // when
            Account result = jsonUtil.parseJson(jsonText, Account.class);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getEmail()).isEqualTo("od@gmail.com");
            assertThat(result.getPassword()).isEqualTo("1234");
            assertThat(result.getUsername()).isEqualTo("od");
            assertThat(result.getUserTel()).isEqualTo("010-1234-5678");
            assertThat(result.getAddress()).isEqualTo("서울시 강남구");
            assertThat(result.getRole()).isNull();
        }

        @Test
        @DisplayName("[error] json 문자열을 객채로 변환하는 중 예외 발생시 RuntimeException 을 응답한다.")
        void error() {
            // given
            String jsonText = "errorJsonText";

            // when
            try {
                jsonUtil.parseJson(jsonText, Account.class);
            } catch (RuntimeException e) {
                // then
                assertThat(e).isInstanceOf(RuntimeException.class);
            }
        }
    }

    @Nested
    @DisplayName("[toJsonString] 객체를 json 문자열로 변환하는 메소드")
    class Describe_toJsonString {

        @Test
        @DisplayName("[success] 객채가 정상적으로 json 문자열로 변환되는지 확인한다.")
        void success() throws JSONException {
            // given
            Account account = Account.builder()
                .id(1L)
                .email("od@gmail.com")
                .password("1234")
                .username("od")
                .userTel("010-1234-5678")
                .address("서울시 강남구")
                .build();

            // when
            String jsonString = jsonUtil.toJsonString(account);
            JSONObject jsonObject = new JSONObject(jsonString);

            // then
            assertThat(jsonObject.get("id")).isEqualTo(1);
            assertThat(jsonObject.get("email")).isEqualTo(account.getEmail());
            assertThat(jsonObject.get("password")).isEqualTo(account.getPassword());
            assertThat(jsonObject.get("username")).isEqualTo(account.getUsername());
            assertThat(jsonObject.get("userTel")).isEqualTo(account.getUserTel());
            assertThat(jsonObject.get("address")).isEqualTo(account.getAddress());
            assertThat(jsonObject.get("role")).isEqualTo(null);
        }
    }

}