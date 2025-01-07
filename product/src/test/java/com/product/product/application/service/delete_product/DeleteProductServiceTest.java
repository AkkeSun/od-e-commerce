package com.product.product.application.service.delete_product;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.IntegrationTestSupport;
import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.in.RegisterProductUseCase;
import com.product.product.application.port.in.command.RegisterProductCommand;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.service.register_product.RegisterProductServiceResponse;
import com.product.product.domain.HistoryType;
import com.product.product.domain.ProductHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class DeleteProductServiceTest extends IntegrationTestSupport {

    @Autowired
    DeleteProductService deleteProductService;
    @Autowired
    RegisterProductUseCase registerProductUseCase;
    @Autowired
    FindProductPort findProductPort;
    @Autowired
    FindProductEsPort findProductEsPort;
    @Autowired
    DeleteProductPort deleteProductPort;

    @BeforeEach
    void setUp() {
        deleteProductPort.deleteAll();
    }

    @Nested
    @DisplayName("[deleteProduct] 상품을 삭제하는 메소드")
    class Describe_deleteProduct {

        @Test
        @DisplayName("[error] 조회된 상품이 없는경우 에러 메시지를 응답한다.")
        void error1() {
            // given
            String authentication = createAccessToken("ROLE_SELLER");
            Long productId = 123L;

            // when
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
                deleteProductService.deleteProduct(productId, authentication);
            });

            // then
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_PROUCT_INFO);
        }

        @Test
        @DisplayName("[error] 상품 판매자와 요청자가 다른 경우 에러 메시지를 응답한다.")
        void error2() {
            // given
            String authentication = createAccessToken("ROLE_CUSTOMER");
            Long productId = saveProduct(authentication);
            String anotherAuthentication = createAccessToken("ROLE_CUSTOMER", 10L);

            // when
            CustomAuthorizationException exception = assertThrows(
                CustomAuthorizationException.class, () -> {
                    deleteProductService.deleteProduct(productId, anotherAuthentication);
                });

            // then
            assert exception.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[success] 상품 판매자와 요청자가 같은 경우 상품을 삭제하고 히스토리를 등록한다.")
        void success() {
            // given
            String authentication = createAccessToken("ROLE_SELLER");
            Long productId = saveProduct(authentication);

            // when
            DeleteProductServiceResponse result = deleteProductService
                .deleteProduct(productId, authentication);
            CustomNotFoundException exception = assertThrows(CustomNotFoundException.class, () -> {
                findProductPort.findById(productId);
            });
            ProductHistory history = findProductPort.findHistoryByProductIdAndAccountId(productId,
                1L).getFirst();

            // then
            assert result.result().equals("Y");
            assert exception.getErrorCode().equals(ErrorCode.DoesNotExist_PROUCT_INFO);
            assert history.getType().equals(HistoryType.DELETE);
        }

        Long saveProduct(String authentication) {
            RegisterProductCommand command = RegisterProductCommand.builder()
                .authorization(authentication)
                .productName("test product")
                .productImg("test img")
                .description("test description")
                .price(10000)
                .quantity(10)
                .category("BOOKS")
                .build();
            RegisterProductServiceResponse response = registerProductUseCase
                .registerProduct(command);
            return response.productId();
        }
    }
}