package com.product_agent.log.adapter.out.persistence.jpa.shard1;

import org.springframework.data.jpa.repository.JpaRepository;

interface DlqLogShard1Repository extends JpaRepository<DlqLogShard1Entity, Long> {

}
