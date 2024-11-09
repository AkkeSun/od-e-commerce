package com.sweettracker.account.token.application.port.out;

public interface DeleteTokenPort {

    void deleteByEmail(String email);
}
