package com.sweettracker.account.account.adapter.out.producer;

import com.sweettracker.account.account.application.port.out.ProduceAccountPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AccountProducerAdapter implements ProduceAccountPort {

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
