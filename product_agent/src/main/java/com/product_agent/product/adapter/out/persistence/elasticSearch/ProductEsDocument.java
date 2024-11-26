package com.product_agent.product.adapter.out.persistence.elasticSearch;

import com.product_agent.product.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor
@Document(indexName = "product")
public class ProductEsDocument {

    @Id
    private Long productId;

    @Field(type = FieldType.Text)
    private String productName;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Text)
    private String sellerEmail;

    @Field(type = FieldType.Text, index = false)
    private String productImg;

    @Field(type = FieldType.Long, index = false)
    private long price;

    @Field(type = FieldType.Long)
    private long salesCount;

    @Field(type = FieldType.Long)
    private long reviewCount;

    @Field(type = FieldType.Double)
    private double totalScore;

    @Field(type = FieldType.Text)
    private Category category;

    @Field(type = FieldType.Text)
    private String regDateTime;

    @Builder
    public ProductEsDocument(Long productId, String productName, String description,
        String sellerEmail,
        String productImg, long price, long salesCount, long reviewCount, double totalScore,
        Category category, String regDateTime) {
        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.sellerEmail = sellerEmail;
        this.productImg = productImg;
        this.price = price;
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.totalScore = totalScore;
        this.category = category;
        this.regDateTime = regDateTime;
    }
}
