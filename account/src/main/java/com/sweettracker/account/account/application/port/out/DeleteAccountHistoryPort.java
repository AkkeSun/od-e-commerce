package com.sweettracker.account.account.application.port.out;

public interface DeleteAccountHistoryPort {

    void deleteByEmail(String email);
}
