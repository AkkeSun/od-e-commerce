package com.sweettracker.account.account.application.port.in;

import com.sweettracker.account.account.application.service.find_account.FindAccountServiceResponse;

public interface FindAccountInfoUseCase {

    FindAccountServiceResponse findAccountInfo(String Authorization);
}
