package com.sweettracker.account.account.application.port.in;

import com.sweettracker.account.account.application.port.in.command.RegisterTokenCommand;
import com.sweettracker.account.account.application.service.register_token.RegisterTokenServiceResponse;

public interface RegisterTokenUseCase {

    RegisterTokenServiceResponse registerToken(RegisterTokenCommand command);
}
