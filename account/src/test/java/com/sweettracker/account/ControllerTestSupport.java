package com.sweettracker.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sweettracker.account.account.application.port.in.FindAccountInfoUseCase;
import com.sweettracker.account.account.application.port.in.RegisterTokenByRefreshUseCase;
import com.sweettracker.account.account.application.port.in.RegisterTokenUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
public class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected FindAccountInfoUseCase findAccountInfoUseCase;

    @MockBean
    protected RegisterTokenUseCase registerTokenUseCase;

    @MockBean
    protected RegisterTokenByRefreshUseCase registerTokenByRefreshUseCase;
}
