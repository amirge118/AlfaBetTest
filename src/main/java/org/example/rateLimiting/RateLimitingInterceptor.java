package org.example.rateLimiting;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import com.google.common.util.concurrent.RateLimiter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RateLimitingInterceptor implements HandlerInterceptor {

    private final RateLimiter rateLimiter;

    public RateLimitingInterceptor(double requestsPerSecond) {
        this.rateLimiter = RateLimiter.create(requestsPerSecond);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Check if a permit is available, otherwise reject the request
        if (!rateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded");
            return false;
        }
        return true;
    }
}
