package com.product.product.adapter.out.persistence.elasticSearch;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.domain.Category;
import com.product.product.domain.Product;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ProductEsDocumentMapperTest {

    ProductEsDocumentMapper mapper = new ProductEsDocumentMapper();

    @Nested
    @DisplayName("[toDocument] 엔티티를 도큐멘트로 변환하는 메소드")
    class Describe_toDocument {

        @Test
        @DisplayName("[success] 엔티티를 도큐멘트로 잘 변환하는지 확인한다.")
        void success() {
            // given
            Product product = Product.builder()
                .productId(1L)
                .productName("상품명")
                .description("상품 설명")
                .sellerEmail("test@gmail.com")
                .productImg("상품 이미지")
                .price(10000L)
                .salesCount(100L)
                .reviewCount(100L)
                .totalScore(4.5)
                .category(Category.ELECTRONICS)
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            ProductEsDocument document = mapper.toDocument(product);

            // then
            assertThat(document.getProductId()).isEqualTo(product.getProductId());
            assertThat(document.getProductName()).isEqualTo(product.getProductName());
            assertThat(document.getDescription()).isEqualTo(product.getDescription());
            assertThat(document.getSellerEmail()).isEqualTo(product.getSellerEmail());
            assertThat(document.getProductImg()).isEqualTo(product.getProductImg());
            assertThat(document.getPrice()).isEqualTo(product.getPrice());
            assertThat(document.getSalesCount()).isEqualTo(product.getSalesCount());
            assertThat(document.getReviewCount()).isEqualTo(product.getReviewCount());
            assertThat(document.getTotalScore()).isEqualTo(product.getTotalScore());
            assertThat(document.getCategory()).isEqualTo(product.getCategory());
            assertThat(document.getRegDateTime()).isEqualTo(
                product.getRegDateTime().format(mapper.formatter));
        }
    }

    @Nested
    @DisplayName("[toDomain] 도큐멘트를 엔티티로 변환하는 메소드")
    class Describe_toDomain {

        @Test
        @DisplayName("[success] 도큐멘트를 엔티티로 잘 변환하는지 확인한다.")
        void success() {
            // given
            ProductEsDocument document = ProductEsDocument.builder()
                .productId(1L)
                .productName("상품명")
                .description("상품 설명")
                .sellerEmail("test@gmail.com")
                .productImg("상품 이미지")
                .price(10000L)
                .salesCount(100L)
                .reviewCount(100L)
                .totalScore(4.5)
                .category(Category.ELECTRONICS)
                .regDateTime(LocalDateTime.now().format(mapper.formatter))
                .build();

            // when
            Product product = mapper.toDomain(document);

            // then
            assertThat(product.getProductId()).isEqualTo(document.getProductId());
            assertThat(product.getProductName()).isEqualTo(document.getProductName());
            assertThat(product.getDescription()).isEqualTo(document.getDescription());
            assertThat(product.getSellerEmail()).isEqualTo(document.getSellerEmail());
            assertThat(product.getProductImg()).isEqualTo(document.getProductImg());
            assertThat(product.getPrice()).isEqualTo(document.getPrice());
            assertThat(product.getSalesCount()).isEqualTo(document.getSalesCount());
            assertThat(product.getReviewCount()).isEqualTo(document.getReviewCount());
            assertThat(product.getTotalScore()).isEqualTo(document.getTotalScore());
            assertThat(product.getCategory()).isEqualTo(document.getCategory());
            assertThat(product.getRegDateTime()).isEqualTo(
                LocalDateTime.parse(document.getRegDateTime(), mapper.formatter));
        }
    }
}