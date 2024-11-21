package com.product.product.application.port.in.command;

import com.product.product.domain.Category;
import com.product.product.domain.ProductSortType;
import java.util.List;
import lombok.Builder;

@Builder
public record FindProductListCommand(
    String keyword,
    ProductSortType sortType,
    Category category,
    int page,
    List<Long> excludeProductIds
) {

    public boolean isSearchKeywordSaveNeeded() {
        return page == 0;
    }
}
