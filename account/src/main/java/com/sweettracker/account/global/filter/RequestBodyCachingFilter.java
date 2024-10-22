package com.sweettracker.account.global.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

/*
    Controller 요청 바디는 한 번만 확인이 가능합니다.

    SpringBoot Validation 에 실패한 경우 필터 레벨에서 BindException 이 발생하기 때문에
    AOP 가 호출되지 않아 요청 데이터를 로깅할 수 없습니다.
    이를 해결하기 위해 요청 바디를 캐시처리한 후 ExceptionAdvice 에서 요청 데이터를 로깅합니다.
 */
public class RequestBodyCachingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        filterChain.doFilter(wrappedRequest, response);
    }
}
