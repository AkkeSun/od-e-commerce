package com.sweettracker.account.account.application.port.in;

import com.sweettracker.account.account.application.port.in.command.RegisterAccountCommand;
import com.sweettracker.account.account.application.service.register_account.RegisterAccountServiceResponse;

public interface RegisterAccountUseCase {

    RegisterAccountServiceResponse registerAccount(RegisterAccountCommand command);
}
