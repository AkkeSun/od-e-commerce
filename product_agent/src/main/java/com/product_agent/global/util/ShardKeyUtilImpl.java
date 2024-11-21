package com.product_agent.global.util;

import org.springframework.stereotype.Component;

@Component
public class ShardKeyUtilImpl implements ShardKeyUtil {

    @Override
    public boolean isShard1(Long snowflakeId) {
        long timestamp = (snowflakeId >> 22) & 0x1FFFFFFFFFFL; // timestamp (41 비트)
        long workerId = (snowflakeId >> 12) & 0x1F; // workerId (5 비트)
        return (timestamp ^ workerId) % 2 == 0;
    }
}
