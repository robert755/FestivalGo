package com.server.backend.user;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class LoginRateLimiterService {

    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public boolean isAllowed(String key) {
        Bucket bucket = cache.computeIfAbsent(key, this::createNewBucket);
        return bucket.tryConsume(1);
    }

    private Bucket createNewBucket(String key) {
        Refill refill = Refill.intervally(5, Duration.ofMinutes(1)); // 5 cereri pe minut
        Bandwidth limit = Bandwidth.classic(5, refill);
        return Bucket4j.builder().addLimit(limit).build();
    }
}
