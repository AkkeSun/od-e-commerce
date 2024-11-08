package com.sweettracker.account.account.adapter.in.delete_account;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.sweettracker.account.RestDocsSupport;
import com.sweettracker.account.account.application.port.in.DeleteAccountUseCase;
import com.sweettracker.account.account.application.service.delete_account.DeleteAccountServiceResponse;
import com.sweettracker.account.global.exception.CustomAuthenticationException;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import com.sweettracker.account.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class DeleteAccountDocsTest extends RestDocsSupport {

    private final DeleteAccountUseCase deleteAccountUseCase
        = mock(DeleteAccountUseCase.class);

    @Override
    protected Object initController() {
        return new DeleteAccountController(deleteAccountUseCase);
    }

    @Nested
    @DisplayName("[deleteAccount] 사용자 정보를 삭제하는 API")
    class Describe_deleteAccount {

        private String tag = "Account";
        private String summary = "사용자 정보 삭제 API";
        private String description = "사용자를 삭제하는 API 입니다. <br>"
            + "테스트시 우측 자물쇠를 클릭하여 유효한 인증 토큰을 입력해야 정상 테스트가 가능합니다. <br>"
            + "(요청 헤더에 인증 토큰을 입력하여 테스트하지 않습니다)";

        @Test
        @DisplayName("[success] 권한 정보가 있는 사용자가 API 를 요청한 경우 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String accessToken = "test-access-token";
            DeleteAccountServiceResponse response = DeleteAccountServiceResponse.builder()
                .id(10L)
                .email("od@google.com")
                .result("Y")
                .build();
            given(deleteAccountUseCase.deleteAccount(accessToken))
                .willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(delete("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
            );

            // then
            actions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("[deleteAccount] SUCCESS",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                                    .description("사용자 아이디"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                    .description("사용자 이메일"),
                                fieldWithPath("data.result").type(JsonFieldType.STRING)
                                    .description("삭제 유무")
                            )
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .responseSchema(Schema.schema("[RESPONSE] delete-account"))
                            .build()
                        )
                    )
                );
        }


        @Test
        @DisplayName("[success] 권한 정보가 없는 사용자가 API 를 호출한 경우 401 코드와 에러 메시지를 응답한다.")
        void error() throws Exception {
            // given
            String authorization = "Bearer invalid-token";
            given(deleteAccountUseCase.deleteAccount(any())).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when
            ResultActions actions = mockMvc.perform(delete("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", authorization)
            );

            // then
            actions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("[deleteAccount] 유효하지 않은 토큰 입력",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
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
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }


        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 404 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
            // given
            String accessToken = "test-access-token";
            given(deleteAccountUseCase.deleteAccount(any()))
                .willThrow(new CustomNotFoundException(ErrorCode.DoesNotExist_ACCOUNT_INFO));

            // when
            ResultActions actions = mockMvc.perform(delete("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", accessToken)
            );

            // then
            actions.andDo(print())
                .andExpect(status().isNotFound())
                .andDo(document("[deleteAccount] 조회된 사용자 정보 없음",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")
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
                            .requestHeaders(headerWithName("Authorization").description("인증 토큰"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }
    }
}