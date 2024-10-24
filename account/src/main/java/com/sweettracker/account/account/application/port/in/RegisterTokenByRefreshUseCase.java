package com.sweettracker.account.account.application.port.in;

import com.sweettracker.account.account.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;

public interface RegisterTokenByRefreshUseCase {

    RegisterTokenByRefreshServiceResponse registerTokenByRefresh(
        String refreshToken);
}
