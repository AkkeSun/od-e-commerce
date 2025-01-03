package com.product.product.adapter.in.find_product;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.review.domain.Review;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductReviewItemTest {

    @Nested
    @DisplayName("[of] 상품 리뷰 도메인을 API 응답 객채로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] 상품 리뷰 도메인이 API 응답 객체로 잘 변환되는지 확인한다.")
        void success() {
            // given
            Review review = Review.builder()
                .id(1L)
                .productId(12345L)
                .accountId(1L)
                .comment("리뷰 내용")
                .score(5)
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            FindProductReviewItem response = new FindProductReviewItem().of(review);

            // then
            assertThat(response.getWriterId()).isEqualTo(review.getAccountId());
            assertThat(response.getScore()).isEqualTo(review.getScore());
            assertThat(response.getComment()).isEqualTo(review.getComment());
            assertThat(response.getRegDateTime()).isEqualTo(
                review.getRegDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }
}