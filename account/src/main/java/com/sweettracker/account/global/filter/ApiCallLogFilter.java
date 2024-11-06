package com.sweettracker.account.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
@Component
public class ApiCallLogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);

        String method = request.getMethod();
        String uri = request.getRequestURI();

        Map<String, Object> requestInfo = new HashMap<>();

        // request parameter
        request.getParameterMap().forEach((key, value) -> {
            requestInfo.put(key, String.join(",", value));
        });
        // request body
        try {
            String requestBody = new String(wrappedRequest.getContentAsByteArray(),
                StandardCharsets.UTF_8);
            requestInfo.putAll(new ObjectMapper().readValue(requestBody, Map.class));
        } catch (Exception ignored) {
        }
        // response body
        String responseBody = new String(wrappedResponse.getContentAsByteArray(),
            StandardCharsets.UTF_8);

        log.info("[{} {}] request - {}", method, uri, requestInfo);
        log.info("[{} {}] response - {}", method, uri, responseBody);

        wrappedResponse.copyBodyToResponse();
    }
}
