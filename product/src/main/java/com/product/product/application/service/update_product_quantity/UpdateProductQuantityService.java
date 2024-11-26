package com.product.product.application.service.update_product_quantity;

import static com.product.global.exception.ErrorCode.Business_OUT_OF_STOCK;

import com.product.global.aop.DistributedLock;
import com.product.global.exception.CustomAuthorizationException;
import com.product.global.exception.CustomBusinessException;
import com.product.global.exception.ErrorCode;
import com.product.global.util.JsonUtil;
import com.product.global.util.JwtUtil;
import com.product.product.application.port.in.UpdateProductSalesUseCase;
import com.product.product.application.port.in.command.UpdateProductSalesCommand;
import com.product.product.application.port.out.FindProductPort;
import com.product.product.application.port.out.ProduceProductPort;
import com.product.product.application.port.out.UpdateProductPort;
import com.product.product.domain.Product;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UpdateProductQuantityService implements UpdateProductSalesUseCase {

    private final JwtUtil jwtUtil;
    private final JsonUtil jsonUtil;
    private final FindProductPort findProductPort;
    private final UpdateProductPort updateProductPort;
    private final ProduceProductPort produceProductPort;

    @Override
    @DistributedLock(key = "updateProductCount")
    public UpdateProductQuantityServiceResponse updateProductSales(
        UpdateProductSalesCommand command) {
        Claims claims = jwtUtil.getClaims(command.authentication());
        Long accountId = claims.get("accountId", Long.class);

        Product product = findProductPort.findById(command.productId());

        if (command.isSale()) {
            if (!product.isAvailableForSale(command.productCount())) {
                throw new CustomBusinessException(Business_OUT_OF_STOCK);
            }
            product.updateSaleCount(command.productCount());
            updateProductPort.updateProductSaleInfo(product, accountId, command.productCount());
            produceProductPort.sendMessage("product-update-sales-topic",
                jsonUtil.toJsonString(product));

        } else {
            if (product.getSellerId() != accountId) {
                throw new CustomAuthorizationException(ErrorCode.ACCESS_DENIED);
            }
            product.addQuantity(command.productCount());
            updateProductPort.updateProductQuantity(product, command.productCount());
        }

        return UpdateProductQuantityServiceResponse.builder()
            .result("Y")
            .build();
    }
}
