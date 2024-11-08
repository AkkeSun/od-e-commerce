package com.sweettracker.account.account.adapter.out.producer;

import static org.assertj.core.api.Assertions.assertThat;

import com.sweettracker.account.IntegrationTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;

class AccountProducerAdapterTest extends IntegrationTestSupport {

    @Autowired
    AccountProducerAdapter producerAdapter;

    @Nested
    @DisplayName("[send] 카프카로 메시지를 전송하는 메소드")
    class Describe_send {

        @Test
        @DisplayName("[success] 메시지를 성공적으로 전송하는 경우 성공 로그가 기록되는지 확인한다.")
        void success(CapturedOutput output) throws InterruptedException {
            // given
            String topic = "test-topic";
            String message = "test-message";

            // when
            producerAdapter.sendMessage(topic, message);
            Thread.sleep(1000);

            // then
            assertThat(output.toString()).contains("[test-topic] ==> test-message");
        }
    }
}