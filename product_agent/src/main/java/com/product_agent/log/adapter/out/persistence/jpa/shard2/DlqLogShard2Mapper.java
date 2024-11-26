package com.product_agent.log.adapter.out.persistence.jpa.shard2;

import com.product_agent.log.domain.DlqLog;
import org.springframework.stereotype.Component;

@Component
class DlqLogShard2Mapper {

    DlqLogShard2Entity toEntity(DlqLog domain) {
        return DlqLogShard2Entity.builder()
            .id(domain.getId())
            .topic(domain.getTopic())
            .payload(domain.getPayload())
            .regDate(domain.getRegDate())
            .regDateTime(domain.getRegDateTime())
            .build();
    }

    DlqLog toDomain(DlqLogShard2Entity entity) {
        return DlqLog.builder()
            .id(entity.getId())
            .topic(entity.getTopic())
            .payload(entity.getPayload())
            .regDate(entity.getRegDate())
            .regDateTime(entity.getRegDateTime())
            .build();
    }
}
