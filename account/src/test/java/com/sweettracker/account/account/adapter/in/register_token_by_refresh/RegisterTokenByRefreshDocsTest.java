package com.sweettracker.account.account.adapter.in.register_token_by_refresh;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.sweettracker.account.RestDocsSupport;
import com.sweettracker.account.account.application.port.in.RegisterTokenByRefreshUseCase;
import com.sweettracker.account.account.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;
import com.sweettracker.account.global.exception.CustomAuthenticationException;
import com.sweettracker.account.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class RegisterTokenByRefreshDocsTest extends RestDocsSupport {

    private final RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase
        = mock(RegisterTokenByRefreshUseCase.class);

    @Override
    protected Object initController() {
        return new RegisterTokenByRefreshController(registerTokenByRefreshUseCase);
    }

    @Nested
    @DisplayName("[registerToken] 리프래시 토큰을 통해 사용자 토큰을 갱신하는 API")
    class Describe_registerToken {

        private String tag = "Account";
        private String summary = "사용자 토큰 갱신 API";
        private String description = "리프래시 토큰을 통해 사용자 토큰을 갱신하는 API";

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            RegisterTokenByRefreshRequest request = RegisterTokenByRefreshRequest
                .builder()
                .refreshToken("valid-refresh-token")
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
            actions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("[registerTokenByRefresh] SUCCESS",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                    .description("리프레시 토큰")
                            )
                            .responseFields(
                                fieldWithPath("httpStatus").type(JsonFieldType.NUMBER)
                                    .description("상태 코드"),
                                fieldWithPath("message").type(JsonFieldType.STRING)
                                    .description("상태 메시지"),
                                fieldWithPath("data").type(JsonFieldType.OBJECT)
                                    .description("응답 데이터"),
                                fieldWithPath("data.accessToken").type(JsonFieldType.STRING)
                                    .description("액세스 토큰"),
                                fieldWithPath("data.refreshToken").type(JsonFieldType.STRING)
                                    .description("리프레시 토큰")
                            )
                            .requestSchema(Schema.schema("[REQUEST] register-token-by-refresh"))
                            .responseSchema(Schema.schema("[RESPONSE] register-token"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 리프래시 토큰을 빈 값으로 않았을 때 400 코드와 오류 메시지를 응답한다.")
        void error1() throws Exception {
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
            actions.andDo(print())
                .andExpect(status().isBadRequest())
                .andDo(document("[registerTokenByRefresh] 리프레시 토큰 미입력",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                    .description("리프레시 토큰")
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
                            .requestSchema(Schema.schema("[REQUEST] register-token-by-refresh"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 유효하지 않은 토큰을 입력했을 때 401 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            RegisterTokenByRefreshRequest request = RegisterTokenByRefreshRequest
                .builder()
                .refreshToken("invalid-refresh-token")
                .build();
            given(registerTokenByRefreshUseCase
                .registerTokenByRefresh(request.getRefreshToken()))
                .willThrow(new CustomAuthenticationException(ErrorCode.INVALID_REFRESH_TOKEN));

            // when
            ResultActions actions = mockMvc.perform(post("/accounts/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
            );

            // then
            actions.andDo(print())
                .andExpect(status().isUnauthorized())
                .andDo(document("[registerTokenByRefresh] 유효하지 않은 리프레시 토큰 입력",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        resource(ResourceSnippetParameters.builder()
                            .tag(tag)
                            .summary(summary)
                            .description(description)
                            .requestFields(
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                    .description("리프레시 토큰")
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
                            .requestSchema(Schema.schema("[REQUEST] register-token-by-refresh"))
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }
    }
}