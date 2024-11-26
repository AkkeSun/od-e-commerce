package com.product_agent.log.adapter.out.persistence.jpa.shard1;

import com.product_agent.log.application.port.out.RegisterDlqLogPort;
import com.product_agent.log.domain.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("primaryTransactionManager")
public class DlqLogShard1Adapter implements RegisterDlqLogPort {

    private final DlqLogShard1Mapper dqlLogMapper;
    private final DlqLogShard1Repository dqlLogRepository;

    @Override
    public DlqLog registerDlqLog(DlqLog dlqLog) {
        DlqLogShard1Entity entity = dqlLogMapper.toEntity(dlqLog);
        return dqlLogMapper.toDomain(dqlLogRepository.save(entity));
    }
}
