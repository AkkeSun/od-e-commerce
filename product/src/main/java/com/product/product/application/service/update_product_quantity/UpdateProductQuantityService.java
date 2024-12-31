package com.product.product.application.service.update_product_quantity;

import static com.product.global.exception.ErrorCode.Business_OUT_OF_STOCK;

import com.product.global.aop.DistributedLock;
import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.CustomBusinessException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.JsonUtil;
import com.product.global.util.JwtUtil;
import com.product.product.application.port.in.UpdateProductQuantityUseCase;
import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.application.port.out.UpdateProductPort;
import com.product.product.domain.Product;
import com.product.product.domain.QuantityType;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateProductQuantityService implements UpdateProductQuantityUseCase {

    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final FindProductPort findProductPort;
    private final UpdateProductPort updateProductPort;
    private final ProduceProductPort produceProductPort;

    @Override
    @DistributedLock(key = "updateProductQuantity")
    public UpdateProductQuantityServiceResponse updateProductQuantity(
        UpdateProductQuantityCommand command) {
        Claims claims = jwtUtil.getClaims(command.authorization());
        Long accountId = claims.get("accountId", Long.class);

        Product product = findProductPort.findById(command.productId());

        if (command.updateType().equals(QuantityType.ADD_QUANTITY)) {
            if (product.getSellerId() != accountId) {
                throw new CustomAuthorizationException(ErrorCode.ACCESS_DENIED);
            }
            product.addQuantity(command.productCount());

        } else if (command.updateType().equals(QuantityType.PURCHASE)) {
            if (!product.isAvailableForSale(command.productCount())) {
                throw new CustomBusinessException(Business_OUT_OF_STOCK);
            }
            product.updatePurchaseCount(command.productCount());

        } else {
            product.updateRefundCount(command.productCount());
        }

        updateProductPort.updateProductQuantity(product, accountId, command);
        //TODO; 로직 검토 필요 (환불의 경우 대응)
        if (!command.updateType().equals(QuantityType.ADD_QUANTITY)) {
            produceProductPort.sendMessage("product-update-quantity-topic",
                jsonUtil.toJsonString(product));
        }

        return UpdateProductQuantityServiceResponse.builder()
            .result("Y")
            .build();
    }
}
