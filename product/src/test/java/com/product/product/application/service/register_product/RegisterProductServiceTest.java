package com.product.product.application.service.register_product;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.IntegrationTestSupport;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.in.command.RegisterProductCommand;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.product.domain.ProductSortType;
import java.util.LinkedHashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class RegisterProductServiceTest extends IntegrationTestSupport {

    @Autowired
    private RegisterProductService registerProductService;
    @Autowired
    private DeleteProductPort deleteProductPort;

    @BeforeEach
    void setUp() {
        resetProductEsIndex.resetIndex();
        deleteProductPort.deleteAll();
    }

    @Nested
    @DisplayName("[registerProduct] 상품을 등록하는 메소드")
    class Describe_registerProduct {

        @Test
        @DisplayName("[success] 상품이 정상적으로 등록되느지 확인한다.")
        void success() throws InterruptedException {
            // given
            RegisterProductCommand command = RegisterProductCommand.builder()
                .authorization(createAccessToken("ROLE_SELLER"))
                .productName("아이폰 12 새상품")
                .productImg("상품 이미지")
                .productOption(List.of("옵션1", "옵션2"))
                .description("애플의 신제품 아이폰 12 입니다.")
                .price(10000)
                .quantity(100)
                .category("ELECTRONICS")
                .build();

            // when
            RegisterProductServiceResponse response = registerProductService
                .registerProduct(command);
            Thread.sleep(1000);
            Product savedData = findProductPort.findById(response.productId());
            LinkedHashSet<Product> savedEsData = findProductEsPort.findByKeyword(
                FindProductListCommand.builder()
                    .keyword("아이폰 12")
                    .sortType(ProductSortType.LATEST)
                    .category(Category.TOTAL)
                    .build());

            // then
            assertThat(response.productId()).isEqualTo(savedData.getProductId());
            assertThat(response.sellerEmail()).isEqualTo(savedData.getSellerEmail());
            assertThat(response.productName()).isEqualTo(savedData.getProductName());
            assertThat(response.productImg()).isEqualTo(savedData.getProductImg());
            assertThat(response.description()).isEqualTo(savedData.getDescription());
            assertThat(response.price()).isEqualTo(savedData.getPrice());
            assertThat(response.quantity()).isEqualTo(savedData.getQuantity());
            assertThat(response.category()).isEqualTo(savedData.getCategory());
            assertThat(savedEsData.size()).isEqualTo(1);
            assertThat(savedEsData.getFirst().getProductId()).isEqualTo(savedData.getProductId());
            assertThat(savedEsData.getFirst().getProductName()).isEqualTo(
                savedData.getProductName());
            assertThat(savedEsData.getFirst().getProductImg()).isEqualTo(savedData.getProductImg());
            assertThat(savedEsData.getFirst().getDescription()).isEqualTo(
                savedData.getDescription());
            assertThat(savedEsData.getFirst().getPrice()).isEqualTo(savedData.getPrice());
        }
    }
}