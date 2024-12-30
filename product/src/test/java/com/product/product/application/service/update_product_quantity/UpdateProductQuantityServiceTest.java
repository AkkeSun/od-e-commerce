package com.product.product.application.service.update_product_quantity;

import static com.product.global.exception.ErrorCode.Business_OUT_OF_STOCK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.product.IntegrationTestSupport;
import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.CustomBusinessException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.in.command.UpdateProductSalesCommand;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class UpdateProductQuantityServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterProductPort registerProductPort;
    @Autowired
    private UpdateProductQuantityService service;
    @Autowired
    private DeleteProductPort deleteProductPort;
    @Autowired
    private FindProductPort findProductPort;

    @BeforeEach
    void setUp() {
        deleteProductPort.deleteAll();
    }

    @Nested
    @DisplayName("[updateProductQuantity] 상품 수량을 수정하는 메소드")
    class Describe_updateProductQuantity {

        @Test
        @DisplayName("[error] 상품 구매이며 현재 상품수와 구매하고자 하는 상품수를 뺀 값이 10 미만이면 예외를 응답한다.")
        void error1() {
            // given
            Product product = registerProductPort.register(Product.builder()
                .productId(snowflakeGenerator.nextId())
                .productName("아이폰 12")
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("애플의 신제품 아이폰 12 입니다.")
                .price(10000)
                .quantity(11)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .embeddingYn("N")
                .category(Category.ELECTRONICS)
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build());
            UpdateProductSalesCommand command = UpdateProductSalesCommand.builder()
                .authorization(createAccessToken("ROLE_CUSTOMER"))
                .productId(product.getProductId())
                .productCount(2)
                .isPurchased(true)
                .build();

            // when
            CustomBusinessException exception = assertThrows(CustomBusinessException.class,
                () -> service.updateProductQuantity(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(Business_OUT_OF_STOCK);
        }

        @Test
        @DisplayName("[success] 상품 구매이며 현재 상품수와 구매하고자 하는 상품수를 뺀 값이 10 이상이면 상품수를 변경하고 카프카 메시지를 발송한다.")
        void success1(CapturedOutput output) {
            // given
            Product product = registerProductPort.register(Product.builder()
                .productId(snowflakeGenerator.nextId())
                .productName("아이폰 12")
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("애플의 신제품 아이폰 12 입니다.")
                .price(10000)
                .quantity(11)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .embeddingYn("N")
                .category(Category.ELECTRONICS)
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build());
            UpdateProductSalesCommand command = UpdateProductSalesCommand.builder()
                .authorization(createAccessToken("ROLE_CUSTOMER"))
                .productId(product.getProductId())
                .productCount(1)
                .isPurchased(true)
                .build();

            // when
            UpdateProductQuantityServiceResponse result = service.updateProductQuantity(command);
            Product updatedProduct = findProductPort.findById(product.getProductId());

            // then
            assertThat(result.result()).isEqualTo("Y");
            assertThat(updatedProduct.getQuantity()).isEqualTo(10);
            assertThat(output.toString().contains("[product-update-sales-topic] ==> ")).isTrue();
        }

        @Test
        @DisplayName("[error] 상품 수량 추가이며 판매자의 아이디와 토큰 사용자의 아이디가 다른 경우 예외를 응답한다.")
        void error2(CapturedOutput output) {
            // given
            Product product = registerProductPort.register(Product.builder()
                .productId(snowflakeGenerator.nextId())
                .productName("아이폰 12")
                .sellerId(2L)
                .sellerEmail("test@gmail.com")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("애플의 신제품 아이폰 12 입니다.")
                .price(10000)
                .quantity(11)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .embeddingYn("N")
                .category(Category.ELECTRONICS)
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build());
            UpdateProductSalesCommand command = UpdateProductSalesCommand.builder()
                .authorization(createAccessToken("ROLE_SELLER"))
                .productId(product.getProductId())
                .productCount(1)
                .isPurchased(false)
                .build();

            // when
            CustomAuthorizationException exception = assertThrows(
                CustomAuthorizationException.class, () -> service.updateProductQuantity(command));

            // then
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[success] 상품 수량 추가이며 판매자의 아이디와 토큰 사용자의 아이디가 같은 경우 상품 수량을 업데이트한다.")
        void success2() {
            // given
            Product product = registerProductPort.register(Product.builder()
                .productId(snowflakeGenerator.nextId())
                .productName("아이폰 12")
                .sellerId(1L)
                .sellerEmail("test@gmail.com")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("애플의 신제품 아이폰 12 입니다.")
                .price(10000)
                .quantity(11)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .embeddingYn("N")
                .category(Category.ELECTRONICS)
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build());
            UpdateProductSalesCommand command = UpdateProductSalesCommand.builder()
                .authorization(createAccessToken("ROLE_SELLER"))
                .productId(product.getProductId())
                .productCount(1)
                .isPurchased(false)
                .build();

            // when
            UpdateProductQuantityServiceResponse result = service.updateProductQuantity(command);
            Product updatedProduct = findProductPort.findById(product.getProductId());

            // then
            assertThat(result.result()).isEqualTo("Y");
            assertThat(updatedProduct.getQuantity()).isEqualTo(12);
        }
    }
}