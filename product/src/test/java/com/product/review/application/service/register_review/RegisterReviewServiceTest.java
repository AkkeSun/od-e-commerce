package com.product.review.application.service.register_review;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.product.IntegrationTestSupport;
import com.product.global.exception.CustomBusinessException;
import com.product.global.exception.CustomNotFoundException;
import com.product.global.exception.ErrorCode;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.Category;
import com.product.product.domain.HistoryType;
import com.product.product.domain.Product;
import com.product.product.domain.ProductHistory;
import com.product.review.application.port.in.command.RegisterReviewCommand;
import com.product.review.application.port.out.DeleteReviewPort;
import com.product.review.application.port.out.RegisterReviewPort;
import com.product.review.domain.Review;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

class RegisterReviewServiceTest extends IntegrationTestSupport {

    @Autowired
    RegisterReviewService service;
    @Autowired
    RegisterProductPort registerProductPort;
    @Autowired
    DeleteProductPort deleteProductPort;
    @Autowired
    DeleteReviewPort deleteReviewPort;
    @Autowired
    RegisterReviewPort registerReviewPort;
    @MockBean
    ProduceProductPort produceProductPort;

    @AfterEach
    void reset() {
        deleteProductPort.deleteAll();
    }

    @Nested
    @DisplayName("[registerReview] 리뷰를 등록하는 메소드")
    class Describe_registerReview {

        @Test
        @DisplayName("[error] 존재하는 상품이 없는 경우 예외를 응답한다.")
        void error1() {
            // given
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(-1L)
                .score(5)
                .comment("좋아요")
                .authorization("Bearer " + createAccessToken("ROLE_CUSTOMER"))
                .build();

            // when
            CustomNotFoundException result = assertThrows(CustomNotFoundException.class, () ->
                service.registerReview(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.DoesNotExist_PROUCT_INFO);
        }

        @Test
        @DisplayName("[error] 해당 상품 구매 이력이 없는 사용자라면 예외를 응답한다.")
        void error2() {
            // given
            Product product = Product.builder()
                .productId(snowflakeGenerator.nextId())
                .sellerId(10L)
                .sellerEmail("od")
                .productName("od")
                .productImg("od")
                .description("od")
                .price(10)
                .quantity(1)
                .category(Category.BEAUTY)
                .embeddingYn("N")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build();
            registerProductPort.register(product);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getProductId())
                .score(5)
                .comment("좋아요")
                .authorization("Bearer " + createAccessToken("ROLE_CUSTOMER"))
                .build();

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class, () ->
                service.registerReview(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);
        }

        @Test
        @DisplayName("[error] 해당 상품 구매 이력이 있으나 추후 환불 하였다면 예외를 응답한다.")
        void error3() {
            // given
            Long accountId = 1L;
            Product product = Product.builder()
                .productId(snowflakeGenerator.nextId())
                .sellerId(10L)
                .sellerEmail("od")
                .productName("od")
                .productImg("od")
                .description("od")
                .price(10)
                .quantity(1)
                .category(Category.BEAUTY)
                .embeddingYn("N")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build();
            registerProductPort.register(product);
            ProductHistory history1 = ProductHistory.builder()
                .id(snowflakeGenerator.nextId())
                .productId(product.getProductId())
                .accountId(accountId)
                .type(HistoryType.PURCHASE)
                .detailInfo("1")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now().minusMinutes(1))
                .build();
            ProductHistory history2 = ProductHistory.builder()
                .id(snowflakeGenerator.nextId())
                .productId(product.getProductId())
                .accountId(accountId)
                .type(HistoryType.REFUND)
                .detailInfo("1")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build();
            registerProductPort.registerHistory(history1);
            registerProductPort.registerHistory(history2);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getProductId())
                .score(5)
                .comment("좋아요")
                .authorization("Bearer " + createAccessToken("ROLE_CUSTOMER"))
                .build();

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class, () ->
                service.registerReview(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.ACCESS_DENIED);

            // clean
            deleteReviewPort.deleteByProductIdAndAccountId(product.getProductId(), accountId);
        }

        @Test
        @DisplayName("[error] 이미 리뷰를 등록 했다면 예외를 응답한다.")
        void error4() {
            // given
            Long accountId = 1L;
            Product product = Product.builder()
                .productId(snowflakeGenerator.nextId())
                .sellerId(10L)
                .sellerEmail("od")
                .productName("od")
                .productImg("od")
                .description("od")
                .price(10)
                .quantity(1)
                .category(Category.BEAUTY)
                .embeddingYn("N")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build();
            registerProductPort.register(product);
            ProductHistory history = ProductHistory.builder()
                .id(snowflakeGenerator.nextId())
                .productId(product.getProductId())
                .accountId(accountId)
                .type(HistoryType.PURCHASE)
                .detailInfo("1")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now().minusMinutes(1))
                .build();
            registerProductPort.registerHistory(history);
            Review review = Review.builder()
                .id(snowflakeGenerator.nextId())
                .productId(product.getProductId())
                .accountId(accountId)
                .score(5)
                .comment("좋아요")
                .regDate("20241112")
                .regDateTime(LocalDateTime.now())
                .build();
            registerReviewPort.register(review);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getProductId())
                .score(5)
                .comment("좋아요")
                .authorization("Bearer " + createAccessToken("ROLE_CUSTOMER"))
                .build();

            // when
            CustomBusinessException result = assertThrows(CustomBusinessException.class, () ->
                service.registerReview(command));

            // then
            assert result.getErrorCode().equals(ErrorCode.Business_SAVED_REVIEW_INFO);

            // clean
            deleteReviewPort.deleteByProductIdAndAccountId(product.getProductId(), accountId);
        }

        @Test
        @DisplayName("[success] 리뷰가 정상적으로 등록되고 카프카 메시지를 발송하는지 확인 한다.")
        void error5() {
            // given
            Long accountId = 1L;
            Product product = Product.builder()
                .productId(snowflakeGenerator.nextId())
                .sellerId(10L)
                .sellerEmail("od")
                .productName("od")
                .productImg("od")
                .description("od")
                .price(10)
                .quantity(1)
                .category(Category.BEAUTY)
                .embeddingYn("N")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now())
                .build();
            registerProductPort.register(product);
            ProductHistory history = ProductHistory.builder()
                .id(snowflakeGenerator.nextId())
                .productId(product.getProductId())
                .accountId(accountId)
                .type(HistoryType.PURCHASE)
                .detailInfo("1")
                .regDate("20210801")
                .regDateTime(LocalDateTime.now().minusMinutes(1))
                .build();
            registerProductPort.registerHistory(history);
            RegisterReviewCommand command = RegisterReviewCommand.builder()
                .productId(product.getProductId())
                .score(5)
                .comment("좋아요")
                .authorization("Bearer " + createAccessToken("ROLE_CUSTOMER"))
                .build();

            // when
            RegisterReviewServiceResponse result = service.registerReview(command);

            // then
            assert result.accountId().equals(accountId);
            assert result.productId().equals(product.getProductId());
            assert result.score() == 5;
            assert result.comment().equals("좋아요");
            assert result.regDate().equals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
            verify(produceProductPort, times(1)).sendMessage(any(), any());

            // clean
            deleteReviewPort.deleteByProductIdAndAccountId(product.getProductId(), accountId);
        }
    }
}