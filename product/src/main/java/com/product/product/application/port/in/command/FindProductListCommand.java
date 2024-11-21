package com.product.product.application.port.in.command;

import com.product.product.domain.ProductSortType;
import lombok.Builder;

@Builder
public record FindProductListCommand(
    String keyword,
    ProductSortType sortType,
    int page
) {

    public boolean isSearchKeywordSaveNeeded() {
        return page == 0;
    }
}
