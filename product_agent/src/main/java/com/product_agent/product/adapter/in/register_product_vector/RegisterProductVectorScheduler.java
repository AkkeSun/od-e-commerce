package com.product_agent.product.adapter.in.register_product_vector;

import com.product_agent.product.application.port.in.RegisterProductVectorUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
class RegisterProductVectorScheduler {

    private final RegisterProductVectorUseCase registerProductVectorUseCase;

    @Scheduled(initialDelayString = "1000", fixedDelayString = "600000") // 10분
    public void bgfSyncScheduler() {
        registerProductVectorUseCase.registerProductVector();
    }
}
