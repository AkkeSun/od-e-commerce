package com.sweettracker.account.account.adapter.in.update_account;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sweettracker.account.ControllerTestSupport;
import com.sweettracker.account.account.application.service.update_account.UpdateAccountServiceResponse;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class UpdateAccountControllerTest extends ControllerTestSupport {

    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        @Test
        @DisplayName("[success] 요청 정보를 올바르게 입력한 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .username("od")
                .build();
            String accessToken = "test-access-token";
            UpdateAccountServiceResponse response = UpdateAccountServiceResponse.builder()
                .updateYn("Y")
                .updateList(Collections.singletonList("username"))
                .build();
            given(updateAccountUseCase.updateAccount(request.toCommand(accessToken)))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(put("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", accessToken)
            );

            // then
            actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(200))
                .andExpect(jsonPath("$.message").value("OK"))
                .andExpect(jsonPath("$.data").exists())
                .andExpect(jsonPath("$.data.updateYn").value(response.updateYn()))
                .andExpect(jsonPath("$.data.updateList[0]").value(response.updateList().get(0)))
                .andDo(print());
        }


        @Test
        @DisplayName("[error] 비밀번호와 비밀번호 확인이 동일하지 않을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("12345")
                .username("od")
                .userTel("01012345678")
                .address("서울시 강남구")
                .build();
            String accessToken = "test-access-token";

            // when
            ResultActions actions = mockMvc.perform(put("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", accessToken)
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("비밀번호와 비밀번호 확인이 일치하지 않습니다."))
                .andDo(print());
        }

        @Test
        @DisplayName("[error] 올바른 전화번호 형식을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("010123458")
                .address("서울시 강남구")
                .build();
            String accessToken = "test-access-token";

            // when
            ResultActions actions = mockMvc.perform(put("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", accessToken)
            );

            // then
            actions.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.httpStatus").value(400))
                .andExpect(jsonPath("$.message").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.data.errorCode").value(1001))
                .andExpect(jsonPath("$.data.errorMessage").value("올바른 전화번호 형식이 아닙니다."))
                .andDo(print());
        }
    }
}