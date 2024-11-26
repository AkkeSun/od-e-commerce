package com.product_agent.log.adapter.out.persistence.jpa.shard2;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "DLQ_LOG")
@NoArgsConstructor
class DlqLogShard2Entity {

    @Id
    @Column(name = "TABLE_INDEX")
    private Long id;

    @Column(name = "TOPIC")
    private String topic;

    @Column(name = "PAYLOAD")
    private String payload;

    @Column(name = "REG_DATE")
    private String regDate;

    @Column(name = "REG_DATE_TIME")
    private LocalDateTime regDateTime;

    @Builder
    DlqLogShard2Entity(Long id, String topic, String payload, String regDate,
        LocalDateTime regDateTime) {
        this.id = id;
        this.topic = topic;
        this.payload = payload;
        this.regDate = regDate;
        this.regDateTime = regDateTime;
    }
}
