package com.product.product.adapter.out.persistence.elasticSearch;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

interface ProductEsRepository extends ElasticsearchRepository<ProductEsDocument, Long> {

    @Query("""
            {
                "bool": {
                    "should": [
                        {
                            "match_phrase": {
                                "productName": "?0"
                            }
                        },
                        {
                            "match_phrase": {
                                "description": "?0"
                            }
                        }
                    ],
                    "minimum_should_match": 1
                }
            }
        """)
    List<ProductEsDocument> findByKeyword(String keyword, Pageable pageable);

    @Query("""
            {
                "bool": {
                    "must": [
                        {
                            "term": {
                                "category.keyword": "?0"
                            }
                        }
                    ],
                    "should": [
                        {
                            "match_phrase": {
                                "productName": "?1"
                            }
                        },
                        {
                            "match_phrase": {
                                "description": "?1"
                            }
                        }
                    ],
                    "minimum_should_match": 1
                }
            }
        """)
    List<ProductEsDocument> findByKeywordAndCategory(String category, String keyword,
        Pageable pageable);
}
