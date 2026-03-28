package com.secureauth.system.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secureauth.system.dto.response.ErrorResponse;
import com.secureauth.system.util.RateLimitBucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class RateLimitingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final Map<String, RateLimitBucket> buckets = new ConcurrentHashMap<>();

    @Value("${security.rate-limit.capacity:20}")
    private int capacity;

    @Value("${security.rate-limit.window-seconds:60}")
    private long windowSeconds;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String key = request.getMethod() + ":" + request.getRequestURI() + ":" + request.getRemoteAddr();
        RateLimitBucket bucket = buckets.computeIfAbsent(key, ignored -> new RateLimitBucket(capacity, windowSeconds));

        if (!bucket.allowRequest()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.writeValue(response.getWriter(), ErrorResponse.builder()
                    .timestamp(Instant.now())
                    .status(HttpStatus.TOO_MANY_REQUESTS.value())
                    .error(HttpStatus.TOO_MANY_REQUESTS.getReasonPhrase())
                    .message("Too many requests. Please try again later.")
                    .path(request.getRequestURI())
                    .details(List.of())
                    .build());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
