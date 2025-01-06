package com.product.product.adapter.out.persistence.elasticSearch;

import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.application.port.out.DeleteProductEsPort;
import com.product.product.application.port.out.FindProductEsPort;
import com.product.product.application.port.out.RegisterProductEsPort;
import com.product.product.application.port.out.ResetProductEsIndex;
import com.product.product.domain.Category;
import com.product.product.domain.Product;
import com.product.product.domain.ProductSortType;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class ProductEsPersistenceAdapter implements RegisterProductEsPort, FindProductEsPort,
    ResetProductEsIndex, DeleteProductEsPort {

    @Value("${service-constant.product.response-page-size}")
    private int responsePageSize;
    private final ProductEsDocumentMapper productMapper;
    private final ProductEsRepository productEsRepository;

    @Override
    public Product register(Product product) {
        ProductEsDocument savedDocument = productEsRepository.save(
            productMapper.toDocument(product));
        return productMapper.toDomain(savedDocument);
    }

    @Override
    public LinkedHashSet<Product> findByKeyword(FindProductListCommand command) {
        if (command.category().equals(Category.TOTAL)) {
            return productEsRepository.findByKeyword(command.keyword(),
                    makePageRequest(command.page(), responsePageSize,
                        command.sortType())).stream()
                .map(productMapper::toDomain)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        return productEsRepository.findByKeywordAndCategory(command.category().name(),
                command.keyword(),
                makePageRequest(command.page(), responsePageSize, command.sortType()))
            .stream()
            .map(productMapper::toDomain)
            .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private Pageable makePageRequest(int page, int size, ProductSortType sortType) {
        return switch (sortType) {
            case LOWEST_PRICE -> PageRequest.of(page, size, Sort.by(Direction.ASC, "price"));
            case HIGHEST_PRICE -> PageRequest.of(page, size, Sort.by(Direction.DESC, "price"));
            case SALES_VOLUME -> PageRequest.of(page, size, Sort.by(Direction.DESC, "salesCount"));
            case LATEST -> PageRequest.of(page, size, Sort.by(Direction.DESC, "productId"));
            case MOST_REVIEWS -> PageRequest.of(page, size, Sort.by(Direction.DESC, "reviewCount"));
            default -> PageRequest.of(page, size, Sort.by(Direction.DESC, "totalScore"));
        };
    }

    @Override // for test
    public void resetIndex() {
        productEsRepository.deleteAll();
    }

    @Override
    public void deleteById(Long productId) {
        productEsRepository.deleteById(productId);
    }
}
