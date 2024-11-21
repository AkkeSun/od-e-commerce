package com.product.product.adapter.out.producer;

import com.product.product.application.port.out.ProduceProductPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class ProductProducerAdapter implements ProduceProductPort {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message)
            .thenAccept(sendResult -> log.info("[{}] ==> {}", topic, message))
            .exceptionally(ex -> {
                log.error("[{}] failed ==> {} | {}", topic, message, ex.toString());
                return null;
            });
    }
}
