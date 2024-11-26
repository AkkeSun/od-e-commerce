package com.product_agent.log.adapter.out.persistence.jpa;

import com.product_agent.global.util.ShardKeyUtil;
import com.product_agent.log.adapter.out.persistence.jpa.shard1.DlqLogShard1Adapter;
import com.product_agent.log.adapter.out.persistence.jpa.shard2.DlqLogShard2Adapter;
import com.product_agent.log.application.port.out.RegisterDlqLogPort;
import com.product_agent.log.domain.DlqLog;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
@RequiredArgsConstructor
class DlqLogPersistenceAdapter implements RegisterDlqLogPort {

    private final ShardKeyUtil shardKeyUtil;
    private final DlqLogShard1Adapter shard1Adapter;
    private final DlqLogShard2Adapter shard2Adapter;

    @Override
    public DlqLog registerDlqLog(DlqLog dlqLog) {
        return shardKeyUtil.isShard1(dlqLog.getId()) ?
            shard1Adapter.registerDlqLog(dlqLog) : shard2Adapter.registerDlqLog(dlqLog);
    }
}
