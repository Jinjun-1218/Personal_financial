package com.u.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    // 每个IP每秒允许2个请求
    private final ConcurrentHashMap<String, RateLimiter> limiters =
            new ConcurrentHashMap<>();

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        // 仅对登录接口进行限流
        if (!request.getRequestURI().contains("/user/login_v2")) {
            return true;
        }

        String ip = getClientIP(request);
        // 针对登录接口使用更严格的限制：每分钟30次
        RateLimiter limiter = limiters.computeIfAbsent(ip,
                k -> RateLimiter.create(0.5)); // 0.5 permits per second = 30次/分钟

        if (!limiter.tryAcquire()) {
            response.setStatus(429);
            response.setContentType("application/json;charset=UTF-8"); // 添加字符集
            response.setCharacterEncoding("UTF-8"); // 明确设置编码
            response.getWriter().write("{\"code\":429,\"msg\":\"登录操作过于频繁请1分钟后再试\"}");
            return false;
        }
        return true;
    }

    private String getClientIP(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
