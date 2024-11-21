package com.product.product.application.port.out;

public interface ProduceProductPort {

    void sendMessage(String topic, String message);
}
