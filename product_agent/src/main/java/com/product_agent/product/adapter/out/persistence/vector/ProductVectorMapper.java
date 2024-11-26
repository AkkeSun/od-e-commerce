package com.product_agent.product.adapter.out.persistence.vector;

import com.product_agent.product.domain.Product;
import dev.langchain4j.data.document.Metadata;
import org.springframework.stereotype.Component;

@Component
public class ProductVectorMapper {

    public Metadata toMetadata(Product product) {
        Metadata metadata = new Metadata();
        metadata.put("productId", product.getProductId());
        metadata.put("productName", product.getProductName());
        metadata.put("description", product.getDescription());
        metadata.put("sellerEmail", product.getSellerEmail());
        metadata.put("productImg", product.getProductImg());
        metadata.put("price", product.getPrice());
        metadata.put("category", product.getCategory().name());
        metadata.put("regDateTime", String.valueOf(product.getRegDateTime()));
        return metadata;
    }
}
