package com.product.product.adapter.in.find_product_list;

import static org.assertj.core.api.Assertions.assertThat;

import com.product.product.application.port.in.command.FindProductListCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FindProductListRequestTest {

    @Nested
    @DisplayName("[toCommand] 입력 데이터를 command로 변환하는 메소드")
    class Describe_toCommand {

        @Test
        @DisplayName("[success] 입력 데이터가 command 로 잘 변환되는지 확인한다.")
        void success() {
            // given
            FindProductListRequest request = FindProductListRequest.builder()
                .keyword("검색어")
                .sortType("RECOMMENDED")
                .page(1)
                .category("AUTOMOTIVE")
                .excludeProductIds(null)
                .build();

            // when
            FindProductListCommand command = request.toCommand();

            // then
            assertThat(command.keyword()).isEqualTo(request.getKeyword());
            assertThat(command.sortType().name()).isEqualTo(request.getSortType());
            assertThat(command.category().name()).isEqualTo(request.getCategory());
            assertThat(command.page()).isEqualTo(request.getPage());
            assertThat(command.excludeProductIds()).isEqualTo(request.getExcludeProductIds());
        }
    }
}