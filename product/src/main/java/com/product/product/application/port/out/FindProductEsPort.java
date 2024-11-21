package com.product.product.application.port.out;

import com.product.product.domain.Category;
import com.product.product.domain.Product;
import java.util.LinkedHashSet;
import java.util.Set;
import org.springframework.data.domain.PageRequest;

public interface FindProductEsPort {

    LinkedHashSet<Product> findByKeyword(String keyword, PageRequest pageRequest);

    LinkedHashSet<Product> findByTags(Set<String> tags, PageRequest pageRequest);

    LinkedHashSet<Product> findByCategory(Category category, PageRequest pageRequest);

    LinkedHashSet<Product> findAll(PageRequest pageRequest);
}
