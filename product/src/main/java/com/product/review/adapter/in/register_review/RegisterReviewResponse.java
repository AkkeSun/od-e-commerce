package com.product.review.adapter.in.register_review;

import com.product.review.application.service.register_review.RegisterReviewServiceResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterReviewResponse {

    private Long id;
    private Long productId;
    private Long accountId;
    private int score;
    private String comment;
    private String regDate;

    @Builder
    RegisterReviewResponse(Long id, Long productId, Long accountId, int score,
        String comment, String regDate) {
        this.id = id;
        this.productId = productId;
        this.accountId = accountId;
        this.score = score;
        this.comment = comment;
        this.regDate = regDate;
    }

    RegisterReviewResponse of(RegisterReviewServiceResponse serviceResponse) {
        return RegisterReviewResponse.builder()
            .id(serviceResponse.id())
            .productId(serviceResponse.productId())
            .accountId(serviceResponse.accountId())
            .score(serviceResponse.score())
            .comment(serviceResponse.comment())
            .regDate(serviceResponse.regDate())
            .build();
    }
}
