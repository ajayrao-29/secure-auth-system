package com.secureauth.system.util;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class RateLimitBucket {

    private final int capacity;
    private final long windowSeconds;
    private final AtomicInteger requestCount;
    private volatile Instant windowStart;

    public RateLimitBucket(int capacity, long windowSeconds) {
        this.capacity = capacity;
        this.windowSeconds = windowSeconds;
        this.requestCount = new AtomicInteger(0);
        this.windowStart = Instant.now();
    }

    public synchronized boolean allowRequest() {
        Instant now = Instant.now();
        if (windowStart.plusSeconds(windowSeconds).isBefore(now)) {
            windowStart = now;
            requestCount.set(0);
        }

        return requestCount.incrementAndGet() <= capacity;
    }
}
