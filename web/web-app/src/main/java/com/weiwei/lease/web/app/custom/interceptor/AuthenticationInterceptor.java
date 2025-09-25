package com.weiwei.lease.web.app.custom.interceptor;

import com.weiwei.lease.common.login.LoginUser;
import com.weiwei.lease.common.login.LoginUserHolder;
import com.weiwei.lease.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 放行OPTIONS预检请求
        if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
            return true;
        }

        // 2. 从请求头获取token
        String token = request.getHeader("access-token");

        // 3. 校验并解析token
        try {
            // 如果token为null或无效，JwtUtil会抛出异常
            Claims claims = JwtUtil.parseToken(token);
            Long userId = claims.get("userId", Long.class);
            String username = claims.get("username", String.class);
            LoginUserHolder.setLoginUser(new LoginUser(userId, username));
            return true; // token有效，放行
        } catch (Exception e) {
            // 捕获所有解析token时发生的异常（包括token为null、过期、伪造等）
            // 统一响应401并拦截
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false; // 拦截请求
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        LoginUserHolder.clear();
    }
}
