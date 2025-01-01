package com.product.review.application.port.in;

import com.product.review.application.port.in.command.RegisterReviewCommand;
import com.product.review.application.service.register_review.RegisterReviewServiceResponse;

public interface RegisterReviewUseCase {

    RegisterReviewServiceResponse registerReview(RegisterReviewCommand command);
}
