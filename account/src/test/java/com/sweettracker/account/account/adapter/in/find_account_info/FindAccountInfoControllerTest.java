package com.sweettracker.account.account.adapter.in.find_account_info;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sweettracker.account.ControllerTestSupport;
import com.sweettracker.account.account.application.service.find_account_info.FindAccountServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class FindAccountInfoControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[FindAccountInfo] 사용자 정보를 조회하는 API")
    class Describe_FindAccountInfo {

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer test-token";
            FindAccountServiceResponse response = FindAccountServiceResponse.builder()
                .id(1L)
                .username("od")
                .userTel("01012341234")
                .address("서울특별시 송파구")
                .email("test@google.com")
                .role("ROLE_CUSTOMER")
                .build();
            given(findAccountInfoUseCase.findAccountInfo(authorization)).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(get("/accounts/info")
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