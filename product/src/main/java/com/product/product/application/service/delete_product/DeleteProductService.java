package com.product.product.application.service.delete_product;

import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.JwtUtil;
import com.product.global.util.SnowflakeGenerator;
import com.product.product.application.port.in.DeleteProductUseCase;
import com.product.product.application.port.out.DeleteProductEsPort;
import com.product.product.application.port.out.DeleteProductPort;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.RegisterProductPort;
import com.product.product.domain.HistoryType;
import com.product.product.domain.Product;
import com.product.product.domain.ProductHistory;
import com.product.review.application.port.out.DeleteReviewPort;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
class DeleteProductService implements DeleteProductUseCase {

    private final SnowflakeGenerator snowflakeGenerator;
    private final JwtUtil jwtUtil;
    private final FindProductPort findProductPort;
    private final DeleteProductPort deleteProductPort;
    private final DeleteProductEsPort deleteProductEsPort;
    private final DeleteReviewPort deleteReviewPort;
    private final RegisterProductPort registerProductPort;

    @Override
    public DeleteProductServiceResponse deleteProduct(Long productId, String authentication) {
        Long accountId = jwtUtil.getAccountId(authentication);
        Product product = findProductPort.findById(productId);
        if (product.getSellerId() != accountId) {
            throw new CustomAuthorizationException(ErrorCode.ACCESS_DENIED);
        }

        deleteReviewPort.deleteByProductId(productId);
        deleteProductPort.deleteById(productId);
        deleteProductEsPort.deleteById(productId);

        registerProductPort.registerHistory(ProductHistory.builder()
            .id(snowflakeGenerator.nextId())
            .productId(productId)
            .accountId(accountId)
            .type(HistoryType.DELETE)
            .detailInfo(product.getProductName())
            .regDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
            .regDateTime(LocalDateTime.now())
            .build());
        return DeleteProductServiceResponse.builder()
            .result("Y")
            .build();
    }
}
