package com.product.product.application.service.register_product;

import static com.product.global.exception.ErrorCode.Business_ES_PRODUCT_SAVE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.product.global.exception.CustomBusinessException;
import com.product.global.util.JwtUtil;
import com.product.global.util.SnowflakeGeneratorImpl;
import com.product.product.application.port.in.command.RegisterProductCommand;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.RegisterProductEsPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Product;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegisterProductServiceErrorTest {

    @InjectMocks
    RegisterProductService service;
    @Spy
    SnowflakeGeneratorImpl snowflakeGenerator;
    @Mock
    JwtUtil jwtUtil;
    @Mock
    DeleteProductPort deleteProductPort;
    @Mock
    RegisterProductPort registerProductPort;
    @Mock
    RegisterProductEsPort registerProductEsPort;

    @Nested
    @DisplayName("[registerProduct] 상품을 등록하는 메소드")
    class Describe_registerProduct {

        @Test
        @DisplayName("[error] 상품 등록중 엘라스틱 서치 오류 발생시 롤백되어 RDB 데이터가 삭제되는지 확인한다.")
        void error() {
            // given
            RegisterProductCommand command = RegisterProductCommand.builder()
                .authorization("bearer token")
                .productName("아이폰 12")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("애플의 신제품 아이폰 12 입니다.")
                .price(10000)
                .quantity(100)
                .category("ELECTRONICS")
                .build();
            Claims claims = Jwts.claims().setSubject("od@sweettracker.co.kr");
            claims.put("accountId", 1);
            claims.put("role", "ROLE_SELLER");

            when(jwtUtil.getClaims(any())).thenReturn(claims);
            when(registerProductPort.register(any())).thenReturn(
                Product.builder().productId(1L).build());
            when(registerProductEsPort.register(any())).thenThrow(new RuntimeException("error"));

            // when
            CustomBusinessException exception = assertThrows(CustomBusinessException.class, () ->
                service.registerProduct(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(Business_ES_PRODUCT_SAVE);
            verify(deleteProductPort, times(1)).deleteById(any());
        }
    }
}