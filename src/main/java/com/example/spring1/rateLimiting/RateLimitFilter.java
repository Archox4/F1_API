package com.example.spring1.rateLimiting;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String RATE_LIMIT_KEY_PREFIX = "rate-limit:";

    private final RateLimitService rateLimitService;

    public RateLimitFilter(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (path.startsWith("/api/")) {
            Bucket bucket = rateLimitService.resolveBucket(buildRateLimitKey(request));

            ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
            if (probe.isConsumed()) {
                response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
                filterChain.doFilter(request, response);
            } else {
                long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
                response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType("text/plain");
                response.getWriter().write("Too many requests. Please try again in " + waitForRefill + " seconds.");
            }
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String buildRateLimitKey(HttpServletRequest request) {
        return RATE_LIMIT_KEY_PREFIX + request.getRemoteAddr().replace(':', '_');
    }
}
