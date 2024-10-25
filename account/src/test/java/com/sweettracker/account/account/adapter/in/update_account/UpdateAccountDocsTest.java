package com.sweettracker.account.account.adapter.in.update_account;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.sweettracker.account.RestDocsSupport;
import com.sweettracker.account.account.application.port.in.UpdateAccountUseCase;
import com.sweettracker.account.account.application.service.update_account.UpdateAccountServiceResponse;
import com.sweettracker.account.global.exception.CustomAuthenticationException;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import com.sweettracker.account.global.exception.ErrorCode;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class UpdateAccountDocsTest extends RestDocsSupport {

    private final UpdateAccountUseCase updateAccountUseCase
        = mock(UpdateAccountUseCase.class);

    @Override
    protected Object initController() {
        return new UpdateAccountController(updateAccountUseCase);
    }

    @Nested
    @DisplayName("[updateAccount] 사용자 정보를 수정하는 메소드")
    class Describe_updateAccount {

        private String tag = "Account";
        private String summary = "사용자 정보 수정 API";
        private String description = "사용자 정보를 수정하는 API 입니다.";

        @Test
        @DisplayName("[success] 요청 정보를 올바르게 입력한 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01022222323")
                .address("서울시 강남구")
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
            actions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("[updateAccount] SUCCESS",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호").optional(),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인").optional(),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("이름").optional(),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소").optional()
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.updateYn").type(JsonFieldType.STRING)
                                    .description("업데이트 유무 (Y, N)"),
                                fieldWithPath("data.updateList").type(JsonFieldType.ARRAY)
                                    .description("업데이트 목록")
                            )
                            .requestSchema(Schema.schema("[REQUEST] update-account"))
                            .responseSchema(Schema.schema("[RESPONSE] update-account"))
                            .build()
                        )
                    )
                );
        }


        @Test
        @DisplayName("[error] 비밀번호와 비밀번호 확인이 동일하지 않을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("12345")
                .username("od")
                .userTel("01022222323")
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
            actions.andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("[updateAccount] 비밀번호와 비밀번호 확인 불일치",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호").optional(),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인").optional(),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("이름").optional(),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소").optional()
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                                    .description("에러 코드"),
                                fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                                    .description("에러 메시지")
                            )
                            .requestSchema(Schema.schema("[REQUEST] update-account"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 올바른 전화번호 형식을 입력하지 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("010123")
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
            actions.andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("[updateAccount] 유효하지 않은 전화번호 형식 입력",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호").optional(),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인").optional(),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("이름").optional(),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소").optional()
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                                    .description("에러 코드"),
                                fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                                    .description("에러 메시지")
                            )
                            .requestSchema(Schema.schema("[REQUEST] update-account"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 유효하지 않은 토큰을 입력한 경우 401 코드와 오류 메시지를 응답한다.")
        void error3() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01012341234")
                .address("서울시 강남구")
                .build();
            String accessToken = "invalid-access-token";
            given(updateAccountUseCase.updateAccount(request.toCommand(accessToken)))
                .willThrow(new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when
            ResultActions actions = mockMvc.perform(put("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", accessToken)
            );

            // then
            actions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("[updateAccount] 유효하지 않은 토큰 입력",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호").optional(),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인").optional(),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("이름").optional(),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소").optional()
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                                    .description("에러 코드"),
                                fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                                    .description("에러 메시지")
                            )
                            .requestSchema(Schema.schema("[REQUEST] update-account"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 404 코드와 오류 메시지를 응답한다.")
        void error4() throws Exception {
            // given
            UpdateAccountRequest request = UpdateAccountRequest.builder()
                .password("1234")
                .passwordCheck("1234")
                .username("od")
                .userTel("01012341234")
                .address("서울시 강남구")
                .build();
            String accessToken = "access-token-2";
            given(updateAccountUseCase.updateAccount(request.toCommand(accessToken)))
                .willThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_ACCOUNT_INFO));

            // when
            ResultActions actions = mockMvc.perform(put("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", accessToken)
            );

            // then
            actions.andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("[updateAccount] 조회된 사용자 정보 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .requestFields(
                                fieldWithPath("password").type(JsonFieldType.STRING)
                                    .description("비밀번호").optional(),
                                fieldWithPath("passwordCheck").type(JsonFieldType.STRING)
                                    .description("비밀번호 확인").optional(),
                                fieldWithPath("username").type(JsonFieldType.STRING)
                                    .description("이름").optional(),
                                fieldWithPath("userTel").type(JsonFieldType.STRING)
                                    .description("전화번호").optional(),
                                fieldWithPath("address").type(JsonFieldType.STRING)
                                    .description("주소").optional()
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.errorCode").type(JsonFieldType.NUMBER)
                                    .description("에러 코드"),
                                fieldWithPath("data.errorMessage").type(JsonFieldType.STRING)
                                    .description("에러 메시지")
                            )
                            .requestSchema(Schema.schema("[REQUEST] update-account"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }
    }
}