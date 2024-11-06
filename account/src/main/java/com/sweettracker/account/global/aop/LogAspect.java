package com.sweettracker.account.global.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final HttpServletRequest request;

    @Pointcut("@annotation(io.micrometer.tracing.annotation.NewSpan))")
    private void newSpan() {
    }

    @Around("newSpan()")
    public Object newSpanLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String httpMethod = request.getMethod();
        String path = request.getRequestURI();
        String[] packages = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        String className = packages[packages.length - 1];
        String methodName = joinPoint.getSignature().getName();

        log.info("[{} {}] {}.{}() call", httpMethod, path, className, methodName);
        return joinPoint.proceed();
    }
}
