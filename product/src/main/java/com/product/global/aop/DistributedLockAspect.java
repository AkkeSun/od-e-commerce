package com.product.global.aop;

import com.product.product.application.port.in.command.UpdateProductQuantityCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1) // @Transactional 보다 먼저 실행 되도록
@RequiredArgsConstructor
public class DistributedLockAspect {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";
    private final RedissonClient redissonClient;

    @Around("@annotation(distributedLock) && args(command)")
    public Object lock(final ProceedingJoinPoint joinPoint, DistributedLock distributedLock,
        UpdateProductQuantityCommand command)
        throws Throwable {

        String key = REDISSON_LOCK_PREFIX + distributedLock.key() + command.productId();
        RLock rLock = redissonClient.getLock(key);

        try {
            boolean available = rLock.tryLock(
                distributedLock.waitTime(),
                distributedLock.leaseTime(),
                distributedLock.timeUnit());

            if (!available) {
                log.info("Redisson Lock is already in use: " + key);
                return false;
            }
            return joinPoint.proceed();

        } catch (InterruptedException e) {
            throw new InterruptedException();
        } finally {
            try {
                rLock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.error("Redisson Lock Already UnLock: " + key);
            }
        }
    }
}
