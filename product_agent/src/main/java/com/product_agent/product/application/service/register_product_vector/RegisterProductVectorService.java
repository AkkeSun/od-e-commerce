package com.product_agent.product.application.service.register_product_vector;

import com.product_agent.product.application.port.in.RegisterProductVectorUseCase;
import com.product_agent.product.application.port.out.DeleteProductVectorPort;
import com.product_agent.product.application.port.out.FindProductPort;
import com.product_agent.product.application.port.out.RegisterProductVectorPort;
import com.product_agent.product.application.port.out.UpdateProductPort;
import com.product_agent.product.domain.Product;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
class RegisterProductVectorService implements RegisterProductVectorUseCase {

    private final FindProductPort findProductPort;

    private final UpdateProductPort updateProductPort;

    private final RegisterProductVectorPort registerProductVectorPort;

    private final DeleteProductVectorPort deleteProductVectorPort;
    private final String documentTemplate = """
            이 상품의 이름은 %s 이고, %s 카테고리에 속해 있습니다.\s
            상품의 가격은 %d원 입니다.\s
            %s
        """;

    @Override
    public void registerProductVector() {
        List<Product> productList = findProductPort.findByEmbeddingYn("N");
        if (productList.isEmpty()) {
            log.info("registerProductVector total -- 0");
            return;
        }

        AtomicInteger successCount = new AtomicInteger();
        productList.forEach(product -> {
            try {
                boolean isRdbUpdated = false;

                String document = String.format(documentTemplate, product.getProductName(),
                    product.getCategory(), product.getPrice(), product.getDescription());

                String vectorId = registerProductVectorPort
                    .registerProductVector(document, product);

                if (StringUtils.hasText(vectorId)) {
                    isRdbUpdated = updateProductPort
                        .updateEmbeddingYnByProductId(product.getProductId(), "Y");
                }
                if (!isRdbUpdated) {
                    deleteProductVectorPort.deleteProductVector(vectorId);
                }

                successCount.getAndIncrement();
            } catch (Exception e) {
                log.error("registerProductVector -- " + product.getProductId(), e);
            }
        });

        log.info("registerProductVector total -- " + successCount.get());
    }
}
