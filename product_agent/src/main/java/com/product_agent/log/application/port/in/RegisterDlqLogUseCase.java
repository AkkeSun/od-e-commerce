package com.product_agent.log.application.port.in;

import com.product_agent.log.application.port.in.command.RegisterDlqLogCommand;

public interface RegisterDlqLogUseCase {

    void RegisterDlqLog(RegisterDlqLogCommand command);
}
