package com.product_agent.log.application.port.in.command;

import lombok.Builder;

@Builder
public record RegisterDlqLogCommand(
    String topic,
    String payload
) {

}
