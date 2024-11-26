package com.product_agent.product.application.service.update_product_sales_count;

import com.product_agent.global.util.JsonUtil;
import com.product_agent.product.application.port.in.UpdateProductSalesCountUseCase;
import com.product_agent.product.application.port.out.FindProductEsPort;
import com.product_agent.product.application.port.out.UpdateProductEsPort;
import com.product_agent.product.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class UpdateProductSalesCountService implements UpdateProductSalesCountUseCase {

    private final JsonUtil jsonUtil;
    private final FindProductEsPort findProductEsPort;
    private final UpdateProductEsPort updateProductEsPort;

    @Override
    public UpdateProductSalesCountServiceResponse updateProductSalesCount(String payload) {
        Product product = jsonUtil.parseJson(payload, Product.class);
        Product savedProduct = findProductEsPort.findById(product.getProductId());

        if (savedProduct.getSalesCount() > product.getSalesCount()) {
            return UpdateProductSalesCountServiceResponse.builder()
                .result("요청 메시지는 최신 정보가 아닙니다.")
                .build();
        }
        updateProductEsPort.updateSalesCount(product);
        return UpdateProductSalesCountServiceResponse.builder()
            .result("Y")
            .build();
    }
}
