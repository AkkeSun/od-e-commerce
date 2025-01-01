package com.product.review.adapter.in.register_review;

import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterReviewRequest {

    @NotNull(message = "평점은 필수값 입니다", groups = NotBlankGroups.class)
    private Integer score;

    @NotBlank(message = "리뷰는 필수값 입니다", groups = NotBlankGroups.class)
    private String comment;

    @Builder
    RegisterReviewRequest(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
