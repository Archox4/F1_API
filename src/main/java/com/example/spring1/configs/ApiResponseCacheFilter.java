package com.example.spring1.configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ApiResponseCacheFilter extends OncePerRequestFilter {

    private static final String CACHE_PREFIX = "api:";

    private final StringRedisTemplate redisTemplate;
    private final Duration ttl;

    public ApiResponseCacheFilter(
            StringRedisTemplate redisTemplate,
            @Value("${api-cache.ttl-seconds:300}") long ttlSeconds
    ) {
        this.redisTemplate = redisTemplate;
        this.ttl = Duration.ofSeconds(ttlSeconds);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (!isCacheableRequest(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String cacheKey = buildCacheKey(request);
        String cachedBody = redisTemplate.opsForValue().get(cacheKey);
        if (cachedBody != null) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader("X-Api-Cache", "HIT");
            response.getWriter().write(cachedBody);
            return;
        }

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(request, responseWrapper);

        if (responseWrapper.getStatus() == HttpStatus.OK.value()) {
            String body = responseBodyAsString(responseWrapper);
            if (!body.isBlank()) {
                redisTemplate.opsForValue().set(cacheKey, body, ttl);
                responseWrapper.setHeader("X-Api-Cache", "MISS");
            }
        }

        responseWrapper.copyBodyToResponse();
    }

    private boolean isCacheableRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        return HttpMethod.GET.matches(request.getMethod())
                && path.startsWith("/api/")
                && !path.startsWith("/api/sync");
    }

    private String buildCacheKey(HttpServletRequest request) {
        String queryString = request.getQueryString();
        String fullPath = queryString == null
                ? request.getRequestURI()
                : request.getRequestURI() + "?" + queryString;

        return CACHE_PREFIX + request.getMethod() + ":" + fullPath;
    }

    private String responseBodyAsString(ContentCachingResponseWrapper responseWrapper) {
        Charset charset = getResponseCharset(responseWrapper);
        return new String(responseWrapper.getContentAsByteArray(), charset);
    }

    private Charset getResponseCharset(ContentCachingResponseWrapper responseWrapper) {
        String encoding = responseWrapper.getCharacterEncoding();
        if (encoding == null) {
            return StandardCharsets.UTF_8;
        }
        return Charset.forName(encoding);
    }
}
