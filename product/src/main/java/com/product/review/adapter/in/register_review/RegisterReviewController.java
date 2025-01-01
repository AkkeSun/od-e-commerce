package com.product.review.adapter.in.register_review;

import com.product.global.response.ApiResponse;
import com.product.global.validation.groups.ValidationSequence;
import com.product.review.application.port.in.RegisterReviewUseCase;
import com.product.review.application.port.in.command.RegisterReviewCommand;
import com.product.review.application.service.register_review.RegisterReviewServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class RegisterReviewController {

    private final RegisterReviewUseCase registerReviewUseCase;

    @PostMapping("/products/{productId}/reviews")
    ApiResponse<RegisterReviewResponse> registerProductReview(
        @Validated(ValidationSequence.class) @RequestBody RegisterReviewRequest request,
        @RequestHeader(name = "Authorization", required = false) String authorization,
        @PathVariable Long productId) {

        RegisterReviewServiceResponse serviceResponse = registerReviewUseCase.registerReview(
            RegisterReviewCommand.builder()
                .productId(productId)
                .score(request.getScore())
                .comment(request.getComment())
                .authorization(authorization)
                .build());
        return ApiResponse.ok(new RegisterReviewResponse().of(serviceResponse));
    }
}
