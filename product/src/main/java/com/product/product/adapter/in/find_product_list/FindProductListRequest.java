package com.product.product.adapter.in.find_product_list;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.product.global.validation.ValidSortType;
import com.product.global.validation.groups.ValidationGroups.CustomGroups;
import com.product.global.validation.groups.ValidationGroups.NotBlankGroups;
import com.product.product.application.port.in.command.FindProductListCommand;
import com.product.product.domain.Category;
import com.product.product.domain.ProductSortType;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
class FindProductListRequest {

    @NotBlank(message = "검색어는 필수값 입니다", groups = NotBlankGroups.class)
    private String keyword;

    @ValidSortType(groups = CustomGroups.class)
    @NotBlank(message = "정렬 타입은 필수값 입니다", groups = NotBlankGroups.class)
    private String sortType;

    private int page;

    private String category;

    private List<Long> excludeProductIds;

    @Builder
    FindProductListRequest(String keyword, String sortType, int page, String category,
        List<Long> excludeProductIds) {
        this.keyword = keyword;
        this.sortType = sortType;
        this.page = page;
        this.category = category;
        this.excludeProductIds = excludeProductIds;
    }

    FindProductListCommand toCommand() {
        return FindProductListCommand.builder()
            .keyword(keyword)
            .sortType(ProductSortType.valueOf(sortType))
            .category(StringUtils.hasText(category) ? Category.valueOf(category) : Category.TOTAL)
            .page(page)
            .excludeProductIds(excludeProductIds)
            .build();
    }

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            return super.toString();
        }
    }
}
