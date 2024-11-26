package com.product_agent.log.application.service.register_dlq_log;

import com.product_agent.global.util.DateUtil;
import com.product_agent.global.util.SnowflakeGenerator;
import com.product_agent.log.application.port.in.RegisterDlqLogUseCase;
import com.product_agent.log.application.port.in.command.RegisterDlqLogCommand;
import com.product_agent.log.application.port.out.RegisterDlqLogPort;
import com.product_agent.log.domain.DlqLog;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class RegisterDlqLogService implements RegisterDlqLogUseCase {

    private final SnowflakeGenerator snowflakeGenerator;
    private final RegisterDlqLogPort registerDqlLogPort;
    private final DateUtil dateUtil;

    @Override
    public void RegisterDlqLog(RegisterDlqLogCommand command) {
        registerDqlLogPort.registerDlqLog(DlqLog.builder()
            .id(snowflakeGenerator.nextId())
            .topic(command.topic())
            .payload(command.payload())
            .regDate(dateUtil.getCurrentDate())
            .regDateTime(LocalDateTime.now())
            .build());
    }
}
