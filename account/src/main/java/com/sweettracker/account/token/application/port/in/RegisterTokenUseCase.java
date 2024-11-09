package com.sweettracker.account.token.application.port.in;

import com.sweettracker.account.token.application.port.in.command.RegisterTokenCommand;
import com.sweettracker.account.token.application.service.register_token.RegisterTokenServiceResponse;

public interface RegisterTokenUseCase {

    RegisterTokenServiceResponse registerToken(RegisterTokenCommand command);
}
