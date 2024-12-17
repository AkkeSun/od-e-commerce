package com.product.product.adapter.out.persistence.elasticSearch;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.IntegrationTestSupport;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.product.domain.ProductSortType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

class ProductEsPersistenceAdapterTest extends IntegrationTestSupport {

    @Autowired
    private ProductEsPersistenceAdapter productEsPersistenceAdapter;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    ProductEsDocument findById(Long id) {
        return elasticsearchOperations.get(
            String.valueOf(id),
            ProductEsDocument.class,
            IndexCoordinates.of("product")
        );
    }

    @AfterEach
    @BeforeEach
    void setUp() {
        IndexOperations indexOperations = elasticsearchOperations.indexOps(ProductEsDocument.class);
        if (indexOperations.exists()) {
            indexOperations.delete();
            indexOperations.create();
        }
    }

    @Nested
    @DisplayName("[register] 상품 데이터를 저장하는 메소드")
    class Describe_register {

        @Test
        @DisplayName("[success] 상품 데이터가 정상적으로 저장되는지 확인한다.")
        void success() throws InterruptedException {
            // given
            Product product = Product.builder()
                .productId(20L)
                .productName("테스트상품명")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.AUTOMOTIVE)
                .embeddingYn("N")
                .productOption(new ArrayList<>())
                .price(10000)
                .quantity(50)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .sellerEmail("12345")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();

            // when
            productEsPersistenceAdapter.register(product);
            Thread.sleep(1000);

            ProductEsDocument savedDocument = findById(product.getProductId());

            // then
            assertThat(savedDocument).isNotNull();
            assertThat(savedDocument.getProductId()).isEqualTo(product.getProductId());
            assertThat(savedDocument.getProductName()).isEqualTo(product.getProductName());
            assertThat(savedDocument.getDescription()).isEqualTo(product.getDescription());
            assertThat(savedDocument.getSellerEmail()).isEqualTo(product.getSellerEmail());
            assertThat(savedDocument.getPrice()).isEqualTo(product.getPrice());
            assertThat(savedDocument.getSalesCount()).isEqualTo(product.getSalesCount());
            assertThat(savedDocument.getReviewCount()).isEqualTo(product.getReviewCount());
            assertThat(savedDocument.getTotalScore()).isEqualTo(product.getTotalScore());
            assertThat(savedDocument.getCategory()).isEqualTo(product.getCategory());
            assertThat(savedDocument.getRegDateTime()).isEqualTo(product.getRegDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
    }

    @Nested
    @DisplayName("[findByKeyword] 키워드 기반으로 상품 정보를 조회하는 메소드")
    class Describe_findByKeyword {

        @Test
        @DisplayName("[success] 상품명에 키워드가 포함되어 있는 경우 해당 상품 정보를 조회하는지 확인한다.")
        void success() throws InterruptedException {
            // given
            Product product1 = Product.builder()
                .productId(21L)
                .productName("od 신발")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.AUTOMOTIVE)
                .embeddingYn("N")
                .productOption(new ArrayList<>())
                .price(10000)
                .quantity(50)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .sellerEmail("12345")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            Product product2 = Product.builder()
                .productId(22L)
                .productName("exg 신발")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.FASHION)
                .embeddingYn("Y")
                .productOption(new ArrayList<>())
                .price(11000)
                .quantity(53)
                .reviewCount(1)
                .reviewScore(4)
                .totalScore(5)
                .hitCount(6)
                .sellerEmail("121212")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            productEsPersistenceAdapter.register(product1);
            productEsPersistenceAdapter.register(product2);
            Thread.sleep(1000);

            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("od")
                .category(Category.TOTAL)
                .page(0)
                .sortType(ProductSortType.LOWEST_PRICE)
                .build();

            // when
            LinkedHashSet<Product> productSet = productEsPersistenceAdapter.findByKeyword(command);

            // then
            assertThat(productSet.size()).isEqualTo(1);
            assertThat(productSet.getFirst().getProductId()).isEqualTo(product1.getProductId());
            assertThat(productSet.getFirst().getProductName()).isEqualTo(product1.getProductName());
            assertThat(productSet.getFirst().getDescription()).isEqualTo(product1.getDescription());
            assertThat(productSet.getFirst().getSellerEmail()).isEqualTo(product1.getSellerEmail());
            assertThat(productSet.getFirst().getPrice()).isEqualTo(product1.getPrice());
            assertThat(productSet.getFirst().getSalesCount()).isEqualTo(product1.getSalesCount());
            assertThat(productSet.getFirst().getReviewCount()).isEqualTo(product1.getReviewCount());
            assertThat(productSet.getFirst().getTotalScore()).isEqualTo(product1.getTotalScore());
            assertThat(productSet.getFirst().getCategory()).isEqualTo(product1.getCategory());
            assertThat(productSet.getFirst().getRegDateTime()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).isEqualTo(
                product1.getRegDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        @Test
        @DisplayName("[success] 상품 설명에 키워드가 포함되어 있는 경우 해당 상품 정보를 조회하는지 확인한다.")
        void success1() throws InterruptedException {
            // given
            Product product1 = Product.builder()
                .productId(23L)
                .productName("od 신발")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.AUTOMOTIVE)
                .embeddingYn("N")
                .productOption(new ArrayList<>())
                .price(10000)
                .quantity(50)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .sellerEmail("12345")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            Product product2 = Product.builder()
                .productId(24L)
                .productName("exg 신발")
                .price(10000)
                .description("스윗트래커의 훌륭한 신발 입니다.")
                .category(Category.FASHION)
                .embeddingYn("Y")
                .productOption(new ArrayList<>())
                .price(11000)
                .quantity(53)
                .reviewCount(1)
                .reviewScore(4)
                .totalScore(5)
                .hitCount(6)
                .sellerEmail("121212")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            productEsPersistenceAdapter.register(product1);
            productEsPersistenceAdapter.register(product2);
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("스윗트래커의")
                .category(Category.TOTAL)
                .page(0)
                .sortType(ProductSortType.LOWEST_PRICE)
                .build();
            Thread.sleep(1000);

            // when
            LinkedHashSet<Product> productSet = productEsPersistenceAdapter.findByKeyword(command);

            // then
            assertThat(productSet.size()).isEqualTo(1);
            assertThat(productSet.getFirst().getProductId()).isEqualTo(product2.getProductId());
            assertThat(productSet.getFirst().getProductName()).isEqualTo(product2.getProductName());
            assertThat(productSet.getFirst().getDescription()).isEqualTo(product2.getDescription());
            assertThat(productSet.getFirst().getSellerEmail()).isEqualTo(product2.getSellerEmail());
            assertThat(productSet.getFirst().getPrice()).isEqualTo(product2.getPrice());
            assertThat(productSet.getFirst().getSalesCount()).isEqualTo(product2.getSalesCount());
            assertThat(productSet.getFirst().getReviewCount()).isEqualTo(product2.getReviewCount());
            assertThat(productSet.getFirst().getTotalScore()).isEqualTo(product2.getTotalScore());
            assertThat(productSet.getFirst().getCategory()).isEqualTo(product2.getCategory());
        }

        @Test
        @DisplayName("[success] 상품 정렬 타입을 입력한 경우 해당 정렬 타입으로 데이터를 조회하는지 확인한다.")
        void success3() throws InterruptedException {
            // given
            Product product1 = Product.builder()
                .productId(23L)
                .productName("od 신발")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.AUTOMOTIVE)
                .embeddingYn("N")
                .productOption(new ArrayList<>())
                .price(10000)
                .quantity(50)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .sellerEmail("12345")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            Product product2 = Product.builder()
                .productId(24L)
                .productName("exg 신발")
                .price(10000)
                .description("스윗트래커의 훌륭한 신발 입니다.")
                .category(Category.FASHION)
                .embeddingYn("Y")
                .productOption(new ArrayList<>())
                .price(11000)
                .quantity(53)
                .reviewCount(1)
                .reviewScore(4)
                .totalScore(5)
                .hitCount(6)
                .sellerEmail("121212")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            productEsPersistenceAdapter.register(product1);
            productEsPersistenceAdapter.register(product2);
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("신발")
                .category(Category.TOTAL)
                .page(0)
                .sortType(ProductSortType.LOWEST_PRICE)
                .build();
            Thread.sleep(1000);

            // when
            LinkedHashSet<Product> productSet = productEsPersistenceAdapter.findByKeyword(command);

            // then
            assertThat(productSet.size()).isEqualTo(2);
            assertThat(productSet.getFirst().getProductId()).isEqualTo(product1.getProductId());
            assertThat(productSet.getFirst().getProductName()).isEqualTo(product1.getProductName());
            assertThat(productSet.getFirst().getDescription()).isEqualTo(product1.getDescription());
            assertThat(productSet.getFirst().getSellerEmail()).isEqualTo(product1.getSellerEmail());
            assertThat(productSet.getFirst().getPrice()).isEqualTo(product1.getPrice());
            assertThat(productSet.getFirst().getSalesCount()).isEqualTo(product1.getSalesCount());
            assertThat(productSet.getFirst().getReviewCount()).isEqualTo(product1.getReviewCount());
            assertThat(productSet.getFirst().getTotalScore()).isEqualTo(product1.getTotalScore());
            assertThat(productSet.getFirst().getCategory()).isEqualTo(product1.getCategory());
        }

        @Test
        @DisplayName("[success] 특정 카테고리를 입력한 경우 특정 카테고리 내에서만 데이터를 조회하는지 확인힌다.")
        void success4() throws InterruptedException {
            // given
            Product product1 = Product.builder()
                .productId(23L)
                .productName("od 신발")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.AUTOMOTIVE)
                .embeddingYn("N")
                .productOption(new ArrayList<>())
                .price(10000)
                .quantity(50)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .sellerEmail("12345")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            Product product2 = Product.builder()
                .productId(24L)
                .productName("exg 신발")
                .price(10000)
                .description("스윗트래커의 훌륭한 신발 입니다.")
                .category(Category.FASHION)
                .embeddingYn("Y")
                .productOption(new ArrayList<>())
                .price(11000)
                .quantity(53)
                .reviewCount(1)
                .reviewScore(4)
                .totalScore(5)
                .hitCount(6)
                .sellerEmail("121212")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            productEsPersistenceAdapter.register(product1);
            productEsPersistenceAdapter.register(product2);
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("신발")
                .category(Category.FASHION)
                .page(0)
                .sortType(ProductSortType.LOWEST_PRICE)
                .build();
            Thread.sleep(1000);

            // when
            LinkedHashSet<Product> productSet = productEsPersistenceAdapter.findByKeyword(command);

            // then
            assertThat(productSet.size()).isEqualTo(1);
            assertThat(productSet.getFirst().getProductId()).isEqualTo(product2.getProductId());
            assertThat(productSet.getFirst().getProductName()).isEqualTo(product2.getProductName());
            assertThat(productSet.getFirst().getDescription()).isEqualTo(product2.getDescription());
            assertThat(productSet.getFirst().getSellerEmail()).isEqualTo(product2.getSellerEmail());
            assertThat(productSet.getFirst().getPrice()).isEqualTo(product2.getPrice());
            assertThat(productSet.getFirst().getSalesCount()).isEqualTo(product2.getSalesCount());
            assertThat(productSet.getFirst().getReviewCount()).isEqualTo(product2.getReviewCount());
            assertThat(productSet.getFirst().getTotalScore()).isEqualTo(product2.getTotalScore());
            assertThat(productSet.getFirst().getCategory()).isEqualTo(product2.getCategory());
        }

        @Test
        @DisplayName("[success] 조회된 정보가 없는경우 빈 LinkedHashSet 을 반환하는지 확인한다.")
        void success5() {
            // given
            Product product = Product.builder()
                .productId(23L)
                .productName("od 신발")
                .price(10000)
                .description("테스트 상품 입니다")
                .category(Category.AUTOMOTIVE)
                .embeddingYn("N")
                .productOption(new ArrayList<>())
                .price(10000)
                .quantity(50)
                .reviewCount(0)
                .reviewScore(0)
                .totalScore(0)
                .hitCount(0)
                .sellerEmail("12345")
                .regDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")))
                .regDateTime(LocalDateTime.now())
                .build();
            productEsPersistenceAdapter.register(product);
            FindProductListCommand command = FindProductListCommand.builder()
                .keyword("ddddddd")
                .category(Category.FASHION)
                .page(0)
                .sortType(ProductSortType.LOWEST_PRICE)
                .build();

            // when
            LinkedHashSet<Product> productSet = productEsPersistenceAdapter.findByKeyword(command);

            // then
            assertThat(productSet.size()).isEqualTo(0);
        }
    }
}