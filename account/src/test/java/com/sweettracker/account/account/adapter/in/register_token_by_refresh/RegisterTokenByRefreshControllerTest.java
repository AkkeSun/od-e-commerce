package com.sweettracker.account.account.adapter.in.register_token_by_refresh;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sweettracker.account.ControllerTestSupport;
import com.sweettracker.account.account.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class RegisterTokenByRefreshControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[registerToken] 리프래시 토큰을 통해 토큰을 갱신하는 API")
    class Describe_registerToken {

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterTokenByRefreshRequest request = RegisterTokenByRefreshRequest
                .builder()
                .refreshToken("test-refresh-token")
                .build();
            RegisterTokenByRefreshServiceResponse response =
                RegisterTokenByRefreshServiceResponse.builder()
                    .accessToken("new-access-token")
                    .refreshToken("new-refresh-token")
                    .build();
            given(registerTokenByRefreshUseCase.registerTokenByRefresh(
                request.getRefreshToken())).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(post("/accounts/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.accessToken").value(response.accessToken()))
                .andExpect(jsonPath("$.data.refreshToken").value(response.refreshToken()))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 리프래시 토큰을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error() throws Exception {
            // given
            RegisterTokenByRefreshRequest request = RegisterTokenByRefreshRequest
                .builder()
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/accounts/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("리프레시 토큰은 필수값 입니다."))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 리프래시 토큰을 빈 값으로 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterTokenByRefreshRequest request = RegisterTokenByRefreshRequest
                .builder()
                .refreshToken("")
                .build();

            // when
            ResultActions actions = mockMvc.perform(post("/accounts/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("리프레시 토큰은 필수값 입니다."))
                .andDo(print());
        }
    }
}