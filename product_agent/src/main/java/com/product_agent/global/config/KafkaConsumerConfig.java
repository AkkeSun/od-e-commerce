package com.product_agent.global.config;

import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Component
public class KafkaConsumerConfig {

    @Value("${kafka.host}")
    private String kafkaHost;

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "product");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String>
    kafkaListenerContainerFactory(ConsumerFactory<String, String> consumerFactory,
        CommonErrorHandler errorHandler) {
        ConcurrentKafkaListenerContainerFactory<String, String> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler); // Kafka 2.5 이하 버전에서는 RetryTemplate 사용
        factory.getContainerProperties()
            .setAckMode(ContainerProperties.AckMode.RECORD); // 재시도 후 자동 커밋 되도록
        return factory;
    }

    @Bean
    public CommonErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        // 재시도 간격: 2초, 최대 재시도 횟수: 3회
        FixedBackOff fixedBackOff = new FixedBackOff(2000L, 3L);

        // 재시도 실패시 핸들링 (Dead Letter Queue)
        DefaultErrorHandler hadler = new DefaultErrorHandler((record, exception) -> {
            if (record != null) {
                String topicName = record.topic() + "-dlq";
                String payload = (String) record.value();

                kafkaTemplate.send(topicName, payload)
                    .exceptionally(ex -> {
                        log.error("[{}] failed ==> {} | {}", topicName, payload, ex.toString());
                        return null;
                    });
            }
        }, fixedBackOff);
        return hadler;
    }
}
