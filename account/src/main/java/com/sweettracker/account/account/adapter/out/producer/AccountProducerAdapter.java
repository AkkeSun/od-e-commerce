package com.sweettracker.account.account.adapter.out.producer;

import com.sweettracker.account.account.application.port.out.ProduceAccountPort;
import com.sweettracker.account.account.domain.Account;
import com.sweettracker.account.global.util.JsonUtil;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class AccountProducerAdapter implements ProduceAccountPort {

    private final JsonUtil jsonUtil;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendAccountDeleteMsgToOrder(Account account) {
        String topic = "account-delete-to-order";
        String message = jsonUtil.toJsonString(account);
        sendMessage(topic, message);
    }

    @Override
    public void sendAccountDeleteMsgToDelivery(Account account) {
        String topic = "account-delete-to-delivery";
        String message = jsonUtil.toJsonString(account);
        sendMessage(topic, message);
    }

    private void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message).thenAccept(new Consumer<SendResult<String, String>>() {
            @Override
            public void accept(SendResult<String, String> sendResult) {
                log.info("[{}] ==> {}", topic, message);
            }
        }).exceptionally(ex -> {
            log.error("[{}] failed ==> {} | {}", topic, message, ex.toString());
            return null;
        });
    }
}
