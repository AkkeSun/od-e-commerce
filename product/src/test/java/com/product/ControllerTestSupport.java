package com.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.global.config.SecurityConfig;
import com.product.global.util.JsonUtil;
import com.product.global.util.JwtUtil;
import com.product.product.application.port.in.FindProductListUseCase;
import com.product.product.application.port.in.RegisterProductUseCase;
import com.product.product.application.port.in.UpdateProductQuantityUseCase;
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

    @MockBean
    protected JsonUtil jsonUtil;

    @MockBean
    protected JwtUtil jwtUtil;

    @MockBean
    protected FindProductListUseCase findProductListUseCase;

    @MockBean
    protected RegisterProductUseCase registerProductUseCase;

    @MockBean
    protected UpdateProductQuantityUseCase updateProductSalesUseCase;

}
