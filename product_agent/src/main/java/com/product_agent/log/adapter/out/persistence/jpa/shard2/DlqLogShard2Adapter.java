package com.product_agent.log.adapter.out.persistence.jpa.shard2;

import com.product_agent.log.application.port.out.RegisterDlqLogPort;
import com.product_agent.log.domain.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional("secondaryTransactionManager")
public class DlqLogShard2Adapter implements RegisterDlqLogPort {

    private final DlqLogShard2Mapper dqlLogMapper;
    private final DlqLogShard2Repository dqlLogRepository;

    @Override
    public DlqLog registerDlqLog(DlqLog dqlLog) {
        DlqLogShard2Entity entity = dqlLogMapper.toEntity(dqlLog);
        return dqlLogMapper.toDomain(dqlLogRepository.save(entity));
    }
}
