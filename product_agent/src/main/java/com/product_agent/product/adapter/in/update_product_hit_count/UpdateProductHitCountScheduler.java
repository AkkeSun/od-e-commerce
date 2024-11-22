package com.product_agent.product.adapter.in.update_product_hit_count;

public class UpdateProductHitCountScheduler {
    /*
        조회수 : MongoDB 적재 -> batch update (RDB, ElasticSearch)
            실시간 정확성이 중요하지 않기 때문에 batch update 로 처리
            쓰기 성능을 고려하여 mongo 사용
        구매수 : kafka 를 이용한 비동기 업데이트 (ElasticSearch)
            실시간 반영이 중요함. 보낼 때 최종 구매수를 보내야함
            업데이트 실패 시 재처리를 위한 DLQ (Dead Letter Queue) 설계 필요
        리뷰수 : kafka 를 이용한 비동기 업데이트 (RDB, ElasticSearch)
            실시간 반영이 중요함. 보낼 때 최종 리뷰수를 보내야함
            업데이트 실패 시 재처리를 위한 DLQ (Dead Letter Queue) 설계 필요
        총점수 : 업데이트 대상을 몽고에 적재 -> batch update (RDB, ElasticSearch)
            대상 : 조회수, 구매수, 리뷰수에 변경 내역이 있는 사용자
     */
}
