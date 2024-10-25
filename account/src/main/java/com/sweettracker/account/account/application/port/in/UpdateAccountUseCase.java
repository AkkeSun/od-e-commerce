package com.sweettracker.account.account.application.port.in;

import com.sweettracker.account.account.application.port.in.command.UpdateAccountCommand;
import com.sweettracker.account.account.application.service.update_account.UpdateAccountServiceResponse;

public interface UpdateAccountUseCase {

    UpdateAccountServiceResponse updateAccount(UpdateAccountCommand command);
}
