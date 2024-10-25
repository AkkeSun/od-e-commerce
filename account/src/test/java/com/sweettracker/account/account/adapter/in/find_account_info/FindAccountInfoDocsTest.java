package com.sweettracker.account.account.adapter.in.find_account_info;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.sweettracker.account.RestDocsSupport;
import com.sweettracker.account.account.application.port.in.FindAccountInfoUseCase;
import com.sweettracker.account.account.application.service.find_account_info.FindAccountServiceResponse;
import com.sweettracker.account.global.exception.CustomAuthenticationException;
import com.sweettracker.account.global.exception.CustomNotFoundException;
import com.sweettracker.account.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class FindAccountInfoDocsTest extends RestDocsSupport {

    private final FindAccountInfoUseCase findAccountInfoUseCase =
        mock(FindAccountInfoUseCase.class);

    @Override
    protected Object initController() {
        return new FindAccountInfoController(findAccountInfoUseCase);
    }

    @Nested
    @DisplayName("[FindAccountInfo] 사용자 정보를 조회하는 API")
    class Describe_FindAccountInfo {

        private String tag = "Account";
        private String summary = "사용자 정보 조회 API";
        private String description = "인증토큰으로 사용자 정보를 조회하는 API 입니다.";

        @Test
        @DisplayName("[success] API 를 호출했을 때 200 코드와 성공 메시지를 응답한다.")
        void success() throws Exception {
            // given
            String authorization = "Bearer test-success-token";
            FindAccountServiceResponse response = FindAccountServiceResponse.builder()
                .id(1L)
                .username("od")
                .userTel("01012341234")
                .address("서울특별시 송파구")
                .email("test@google.com")
                .role("ROLE_CUSTOMER")
                .regDate("20241212")
                .build();
            given(findAccountInfoUseCase.findAccountInfo(authorization)).willReturn(response);

            // when
            ResultActions actions = mockMvc.perform(get("/accounts/info")
                .header("Authorization", authorization));

            // then
            actions.andDo(print())
                .andExpect(status().isOk())
                .andDo(document("[FindAccountInfo] SUCCESS",
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
                                fieldWithPath("data.username").type(JsonFieldType.STRING)
                                    .description("사용자 이름"),
                                fieldWithPath("data.userTel").type(JsonFieldType.STRING)
                                    .description("사용자 전화번호"),
                                fieldWithPath("data.address").type(JsonFieldType.STRING)
                                    .description("사용자 주소"),
                                fieldWithPath("data.email").type(JsonFieldType.STRING)
                                    .description("사용자 이메일"),
                                fieldWithPath("data.role").type(JsonFieldType.STRING)
                                    .description("사용자 권한"),
                                fieldWithPath("data.regDate").type(JsonFieldType.STRING)
                                    .description("등록일")
                            )
                            .responseSchema(Schema.schema("[RESPONSE] find-account-info"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 유효하지 않는 토큰을 입력한 경우 401 코드와 오류 메시지를 응답한다.")
        void error() throws Exception {
            // given
            String authorization = "Bearer invalid-token";
            given(findAccountInfoUseCase.findAccountInfo(authorization)).willThrow(
                new CustomAuthenticationException(ErrorCode.INVALID_ACCESS_TOKEN));

            // when
            ResultActions actions = mockMvc.perform(get("/accounts/info")
                .header("Authorization", authorization));

            // then
            actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andDo(document("[FindAccountInfo] 유효하지 않은 토큰 입력",
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
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }

        @Test
        @DisplayName("[error] 조회된 사용자 정보가 없는 경우 404 코드와 오류 메시지를 응답한다.")
        void error2() throws Exception {
            // given
            String authorization = "Bearer invalid-token";
            given(findAccountInfoUseCase.findAccountInfo(authorization)).willThrow(
                new CustomNotFoundException(ErrorCode.DoesNotExist_ACCOUNT_INFO));

            // when
            ResultActions actions = mockMvc.perform(get("/accounts/info")
                .header("Authorization", authorization));

            // then
            actions.andDo(print())
                .andExpect(status().is4xxClientError())
                .andDo(document("[FindAccountInfo] 조회된 사용자 정보 없음",
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
                            .responseSchema(Schema.schema("[RESPONSE] ERROR"))
                            .build()
                        )
                    )
                );
        }
    }
}