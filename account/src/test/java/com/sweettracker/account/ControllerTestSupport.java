package com.sweettracker.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweettracker.account.account.application.port.in.FindAccountInfoUseCase;
import com.sweettracker.account.account.application.port.in.RegisterAccountUseCase;
import com.sweettracker.account.account.application.port.in.RegisterTokenByRefreshUseCase;
import com.sweettracker.account.account.application.port.in.RegisterTokenUseCase;
import com.sweettracker.account.account.application.port.in.UpdateAccountUseCase;
import com.sweettracker.account.global.config.SecurityConfig;
import com.sweettracker.account.global.filter.ApiCallLogFilter;
import com.sweettracker.account.global.filter.JwtAuthenticationFilter;
import com.sweettracker.account.global.util.JsonUtil;
import com.sweettracker.account.global.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(SecurityConfig.class)
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    protected ApiCallLogFilter apiCallLogFilter;

    @MockBean
    protected JsonUtil jsonUtil;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected FindAccountInfoUseCase findAccountInfoUseCase;

    @MockBean
    protected RegisterTokenUseCase registerTokenUseCase;

    @MockBean
    protected RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase;

    @MockBean
    protected RegisterAccountUseCase registerAccountUseCase;

    @MockBean
    protected UpdateAccountUseCase updateAccountUseCase;

}
