package com.product_agent.product.adapter.in.update_product_sales_count;

import com.product_agent.log.application.port.in.RegisterDlqLogUseCase;
import com.product_agent.log.application.port.in.command.RegisterDlqLogCommand;
import com.product_agent.product.application.port.in.UpdateProductSalesCountUseCase;
import com.product_agent.product.application.service.update_product_sales_count.UpdateProductSalesCountServiceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class UpdateProductSalesCountConsumer {

    private final UpdateProductSalesCountUseCase updateProductSalesCountUseCase;
    private final RegisterDlqLogUseCase registerDlqLogUseCase;

    @KafkaListener(
        topics = "product-update-sales-topic",
        containerFactory = "kafkaListenerContainerFactory",
        concurrency = "2" // 워커 thread 수 (파티션과 동일하게 설정)
    )
    void updateProductSalesCountConsumer(@Payload String payload) {
        log.info("[product-update-sales-topic] <== " + payload);
        UpdateProductSalesCountServiceResponse response = updateProductSalesCountUseCase
            .updateProductSalesCount(payload);
        log.info("[product-update-sales-topic] result - " + response.result());
    }

    @KafkaListener(
        topics = "product-update-sales-topic-dlq",
        containerFactory = "kafkaListenerContainerFactory",
        concurrency = "2"
    )
    void updateProductSalesCountConsumerDlq(@Payload String payload) {
        try {
            log.info("[product-update-sales-topic-dql] <== " + payload);
            UpdateProductSalesCountServiceResponse response = updateProductSalesCountUseCase.updateProductSalesCount(
                payload);
            log.info("[product-update-sales-topic] result - " + response.result());
        } catch (Exception e) {
            log.error("[product-update-sales-topic-dql] error - " + e.getMessage());
            registerDlqLogUseCase.RegisterDlqLog(RegisterDlqLogCommand.builder()
                .topic("product-update-sales-topic-dlq")
                .payload(payload)
                .build());
        }
    }
}
