package com.product.product.adapter.out.persistence.vector;

import com.product.product.domain.Category;
import com.product.product.domain.Product;
import dev.langchain4j.data.document.Metadata;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;

@Component
class ProductVectorMapper {

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    Product toDomain(Metadata metadata) {
        return Product.builder()
            .productId(metadata.getLong("productId"))
            .productName(metadata.getString("productName"))
            .description(metadata.getString("description"))
            .sellerEmail(metadata.getString("sellerEmail"))
            .productImg(metadata.getString("productImg"))
            .price(metadata.getInteger("price"))
            .category(Category.valueOf(metadata.getString("category")))
            .regDateTime(LocalDateTime.parse(metadata.getString("regDateTime"), formatter))
            .build();
    }
}
