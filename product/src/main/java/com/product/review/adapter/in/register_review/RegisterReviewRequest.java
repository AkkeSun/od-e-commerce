package com.product.review.adapter.in.register_review;

import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.global.validation.groups.ValidationGroups.SizeGroups;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
class RegisterReviewRequest {

    @NotNull(message = "평점은 필수값 입니다", groups = NotBlankGroups.class)
    @Min(value = 1, message = "평점은 1점 부터 5점까지만 입력 가능합니다", groups = SizeGroups.class)
    @Max(value = 5, message = "평점은 1점 부터 5점까지만 입력 가능합니다", groups = SizeGroups.class)
    private Integer score;

    @NotBlank(message = "리뷰는 필수값 입니다", groups = NotBlankGroups.class)
    @Size(max = 500, message = "리뷰는 500자를 초과할 수 없습니다", groups = NotBlankGroups.class)
    private String comment;

    @Builder
    RegisterReviewRequest(Integer score, String comment) {
        this.score = score;
        this.comment = comment;
    }
}
