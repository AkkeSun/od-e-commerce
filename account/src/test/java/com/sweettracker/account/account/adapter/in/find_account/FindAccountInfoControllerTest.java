package com.sweettracker.account.account.adapter.in.find_account;

import static com.sweettracker.account.global.exception.ErrorCode.INVALID_ACCESS_TOKEN;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweettracker.account.ControllerTestSupport;
import com.sweettracker.account.account.application.service.find_account.FindAccountServiceResponse;
import com.sweettracker.account.global.exception.ErrorResponse;
import com.sweettracker.account.global.response.ApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;

class FindAccountInfoControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[FindAccountInfo] 사용자 정보를 조회하는 API")
    class Describe_FindAccountInfo {

        @Test
        @WithAnonymousUser
        @DisplayName("[error] 권한이 없는 사용자가 API 를 호출했을 때 401 코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            String authorization = "Bearer invalid-token";
            given(jsonUtil.toJsonString(any())).willReturn(new ObjectMapper().writeValueAsString(
                ApiResponse.of(
                    HttpStatus.UNAUTHORIZED,
                    ErrorResponse.builder()
                        .errorCode(INVALID_ACCESS_TOKEN.getCode())
                        .errorMessage(INVALID_ACCESS_TOKEN.getMessage())
                        .build())));

            // when
            ResultActions actions = mockMvc.perform(get("/accounts")
                .header("Authorization", authorization));

            // then
            actions.andExpect(status().isUnauthorized())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.httpStatus").value(401))
                .andExpect(jsonPath("$.message").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.data.errorCode").value(INVALID_ACCESS_TOKEN.getCode()))
                .andExpect(jsonPath("$.data.errorMessage").value(INVALID_ACCESS_TOKEN.getMessage()))
                .andDo(print());
        }

        @Test
        @WithMockUser(username = "od", roles = "CUSTOMER")
        @DisplayName("[success] 권한이 있는 사용자가 API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer success-token";
            FindAccountServiceResponse response = FindAccountServiceResponse.builder()
                .id(1L)
                .username("od")
                .userTel("01012341234")
                .address("서울특별시 송파구")
                .email("test@google.com")
                .role("ROLE_CUSTOMER")
                .build();
            given(findAccountInfoUseCase.findAccountInfo(authorization)).willReturn(response);
            given(jsonUtil.toJsonString(any())).willReturn("test");

            // when
            ResultActions actions = mockMvc.perform(get("/accounts")
                .header("Authorization", authorization));

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.id").value(response.id()))
                .andExpect(jsonPath("$.data.username").value(response.username()))
                .andExpect(jsonPath("$.data.userTel").value(response.userTel()))
                .andExpect(jsonPath("$.data.address").value(response.address()))
                .andExpect(jsonPath("$.data.email").value(response.email()))
                .andExpect(jsonPath("$.data.role").value(response.role()))
                .andDo(print());
        }
    }
}