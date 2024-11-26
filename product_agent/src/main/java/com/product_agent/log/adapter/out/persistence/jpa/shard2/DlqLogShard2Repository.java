package com.product_agent.log.adapter.out.persistence.jpa.shard2;

import org.springframework.data.jpa.repository.JpaRepository;

interface DlqLogShard2Repository extends JpaRepository<DlqLogShard2Entity, Long> {

}
