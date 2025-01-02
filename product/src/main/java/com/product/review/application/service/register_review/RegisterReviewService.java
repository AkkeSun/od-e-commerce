package com.product.review.application.service.register_review;

import com.product.global.exception.CustomBusinessException;
import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.JsonUtil;
import com.product.global.util.JwtUtil;
import com.product.global.util.SnowflakeGenerator;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.domain.HistoryType;
import com.product.product.domain.ProductHistory;
import com.product.review.application.port.in.RegisterReviewUseCase;
import com.product.review.application.port.in.command.RegisterReviewCommand;
import com.product.review.application.port.out.FindReviewPort;
import com.product.review.application.port.out.RegisterReviewPort;
import com.product.review.domain.Review;
import io.jsonwebtoken.Claims;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterReviewService implements RegisterReviewUseCase {

    private final JsonUtil jsonUtil;
    private final SnowflakeGenerator snowflakeGenerator;
    private final JwtUtil jwtUtil;
    private final FindReviewPort findReviewPort;
    private final FindProductPort findProductPort;
    private final ProduceProductPort produceProductPort;
    private final RegisterReviewPort registerReviewPort;
    @Value("${kafka.topic.register-review}")
    private String topicName;

    @NewSpan
    @Override
    public RegisterReviewServiceResponse registerReview(RegisterReviewCommand command) {
        Claims claims = jwtUtil.getClaims(command.authorization());
        Long accountId = Long.valueOf(claims.get("accountId", Integer.class));

        if (!findProductPort.existsById(command.productId())) {
            throw new CustomNotFoundException(ErrorCode.DoesNotExist_PROUCT_INFO);
        }
        if (accessDenied(accountId, command.productId())) {
            throw new CustomBusinessException(ErrorCode.ACCESS_DENIED);
        }
        if (findReviewPort.existsByProductIdAndAccountId(command.productId(), accountId)) {
            throw new CustomBusinessException(ErrorCode.Business_SAVED_REVIEW_INFO);
        }

        Review savedReviewInfo = registerReviewPort.register(Review.builder()
            .id(snowflakeGenerator.nextId())
            .accountId(accountId)
            .productId(command.productId())
            .score(command.score())
            .comment(command.comment())
            .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build());

        produceProductPort.sendMessage(topicName, jsonUtil.toJsonString(savedReviewInfo));

        return RegisterReviewServiceResponse.builder()
            .id(savedReviewInfo.getId())
            .accountId(savedReviewInfo.getAccountId())
            .productId(savedReviewInfo.getProductId())
            .score(savedReviewInfo.getScore())
            .comment(savedReviewInfo.getComment())
            .regDate(savedReviewInfo.getRegDate())
            .build();
    }

    private boolean accessDenied(Long accountId, Long productId) {
        List<ProductHistory> histories = findProductPort.findHistoryByProductIdAndAccountId(
                productId, accountId)
            .stream()
            .filter(h -> h.getType().equals(HistoryType.PURCHASE) ||
                h.getType().equals(HistoryType.REFUND))
            .sorted((h1, h2) -> h2.getRegDateTime().compareTo(h1.getRegDateTime()))
            .toList();
        return histories.isEmpty() || histories.getFirst().getType().equals(HistoryType.REFUND);
    }
}
