package com.sweettracker.account.token.application.port.in;

import com.sweettracker.account.token.application.service.register_token_by_refresh.RegisterTokenByRefreshServiceResponse;

public interface RegisterTokenByRefreshUseCase {

    RegisterTokenByRefreshServiceResponse registerTokenByRefresh(
        String refreshToken);
}
