package com.example.spring1.rateLimiting;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RateLimitService {

    private final ProxyManager<String> proxyManager;
    private final long capacity;
    private final Duration refillDuration;

    public RateLimitService(ProxyManager<String> proxyManager,
        @Value("${rate-limit.capacity}") long capacity,
        @Value("${rate-limit.time}") long time,
        @Value("${rate-limit.unit}") String unit
    ){
        this.proxyManager = proxyManager;
        this.capacity = capacity;
        this.refillDuration = switch (unit.toLowerCase()){
            case "second", "seconds" -> Duration.ofSeconds(time);
            case "minute", "minutes" -> Duration.ofMinutes(time);
            case "hour", "hours" -> Duration.ofHours(time);
            default -> throw new IllegalArgumentException("Unupported rate limiting time unit: "+ unit);
        };
    }

    public Bucket resolveBucket(String key) {
        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(limit -> limit.capacity(capacity).refillGreedy(capacity, refillDuration))
                .build();

        return proxyManager.builder().build(key, () -> configuration);
    }
}
