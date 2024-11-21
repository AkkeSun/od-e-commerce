package com.product.product.adapter.out.persistence.jpa.shard2;

import com.product.product.domain.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "PRODUCT")
@NoArgsConstructor
public class ProductShard2Entity {

    @Id
    @Column(name = "TABLE_INDEX")
    private long productId;

    @Column(name = "SELLER_ID")
    private long sellerId;

    @Column(name = "SELLER_EMAIL")
    private String sellerEmail;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_IMG")
    private String productImg;

    @Column(name = "PRODUCT_OPTION")
    private String productOption;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "QUANTITY")
    private long quantity;

    @Column(name = "SALES_COUNT")
    private long salesCount;

    @Column(name = "REVIEW_COUNT")
    private long reviewCount;

    @Column(name = "REVIEW_SCORE")
    private double reviewScore;

    @Column(name = "TOTAL_SCORE")
    private double totalScore;

    @Column(name = "EMBEDDING_YN")
    private String embeddingYn;

    @Column(name = "CATEGORY")
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(name = "REG_DATE")
    private String regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    ProductShard2Entity(long productId, long sellerId, String sellerEmail,
        String productName,
        String productImg, String productOption, String description, long price, long quantity,
        long salesCount, long reviewCount, double reviewScore, double totalScore,
        String embeddingYn,
        Category category, String regDate, LocalDateTime regDateTime) {
        this.productId = productId;
        this.sellerId = sellerId;
        this.sellerEmail = sellerEmail;
        this.productName = productName;
        this.productImg = productImg;
        this.productOption = productOption;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.salesCount = salesCount;
        this.reviewCount = reviewCount;
        this.reviewScore = reviewScore;
        this.totalScore = totalScore;
        this.embeddingYn = embeddingYn;
        this.category = category;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}
