package com.product.review.adapter.in.register_review;

import com.product.review.application.service.register_review.RegisterReviewServiceResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RegisterReviewResponseTest {

    @Nested
    @DisplayName("[of] serviceResponse 를 apiResponse 로 변환하는 메소드")
    class Describe_of {

        @Test
        @DisplayName("[success] serviceResponse 를 apiResponse 로 변환한다")
        void success() {
            // Given
            RegisterReviewServiceResponse serviceResponse = RegisterReviewServiceResponse.builder()
                .id(1L)
                .productId(1L)
                .accountId(1L)
                .score(5)
                .comment("comment")
                .regDate("20210801")
                .build();

            // When
            RegisterReviewResponse response = new RegisterReviewResponse().of(serviceResponse);

            // Then
            assert response.getId().equals(1L);
            assert response.getProductId().equals(1L);
            assert response.getAccountId().equals(1L);
            assert response.getScore() == 5;
            assert response.getComment().equals("comment");
            assert response.getRegDate().equals("20210801");
        }
    }
}