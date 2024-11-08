package com.sweettracker.account.account.application.port.in;

import com.sweettracker.account.account.application.service.delete_account.DeleteAccountServiceResponse;

public interface DeleteAccountUseCase {

    DeleteAccountServiceResponse deleteAccount(String authentication);
}
