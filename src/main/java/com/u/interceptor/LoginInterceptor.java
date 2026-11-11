package com.u.interceptor;

import com.u.constant.JwtClaimsConstant;
import com.u.context.BaseContext;
import com.u.properties.JwtProperties;
import com.u.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }
        //检查请求方法
        if (!("POST".equalsIgnoreCase( request.getMethod())
                ||  "GET".equalsIgnoreCase( request.getMethod())
                || "PUT".equalsIgnoreCase( request.getMethod())
                || "DELETE".equalsIgnoreCase( request.getMethod()))
        ){
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED); // 405
            response.getWriter().write("{\"code\": 405, \"message\": \"只允许 POST,GET,PUT,DELETE请求\"}");
            return false; // 拦截非法请求
        }


        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Integer userId = Integer.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);    //设置用户id
            //BaseContext 通常是一个用于管理线程本地（ThreadLocal）变量的工具类
            //BaseContext.setCurrentId(userId)将当前用户的ID存储在ThreadLocal变量中。
            //这意味着，只要线程在执行过程中，就可以通过BaseContext的某个方法（如getCurrentId()）获取到这个用户ID，而不需要每次都从HTTP请求或其他地方重新获取。
            log.info("当前用户id：{}", userId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }
}
