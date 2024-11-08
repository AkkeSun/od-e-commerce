package com.sweettracker.account.account.application.port.out;

public interface ProduceAccountPort {

    void sendMessage(String topic, String message);
}
