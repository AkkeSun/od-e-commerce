package com.product_agent.log.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DlqLog {

    Long id;
    String topic;
    String payload;
    String regDate;
    LocalDateTime regDateTime;

    @Builder
    public DlqLog(Long id, String topic, String payload, String regDate,
        LocalDateTime regDateTime) {
        this.id = id;
        this.topic = topic;
        this.payload = payload;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}

