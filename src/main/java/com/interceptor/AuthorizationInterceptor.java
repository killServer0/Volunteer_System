package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.annotation.IgnoreAuth;
import com.entity.TokenEntity;
import com.service.TokenService;

/**
 * 权限(Token)验证
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String LOGIN_TOKEN_KEY = "Token";

    @Autowired
    private TokenService tokenService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 支持跨域请求
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, PATCH");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, Token");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Expose-Headers", "Authorization, Token");

        // 处理OPTIONS请求
        if (request.getMethod().equals("OPTIONS")) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        String servletPath = request.getServletPath();
        // 添加登录相关的URL到白名单
        if("/dictionary/page".equals(servletPath) 
           || "/file/upload".equals(servletPath) 
           || "/file/download".equals(servletPath)  // 添加文件下载接口
           || "/yonghu/register".equals(servletPath)
           || servletPath.startsWith("/zhiyuanzhe/")  // 志愿者相关的URL
           || servletPath.startsWith("/login/")       // 登录相关的URL
           || servletPath.contains("login.html")      // 登录页面
           || servletPath.contains("register.html")   // 注册页面
           || servletPath.endsWith(".js")            // 静态资源
           || servletPath.endsWith(".css")
           || servletPath.endsWith(".jpg")
           || servletPath.endsWith(".png")
           || servletPath.endsWith(".ico")) {
            return true;
        }

        IgnoreAuth annotation;
        if (handler instanceof HandlerMethod) {
            annotation = ((HandlerMethod) handler).getMethodAnnotation(IgnoreAuth.class);
        } else {
            return true;
        }

        if (annotation != null) {
            return true;
        }

        String token = request.getHeader(LOGIN_TOKEN_KEY);
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(LOGIN_TOKEN_KEY);
        }

        if (StringUtils.isBlank(token)) {
            throw new RuntimeException("请先登录");
        }

        TokenEntity tokenEntity = tokenService.getTokenEntity(token);
        if (tokenEntity == null || tokenEntity.getExpiratedtime().getTime() < System.currentTimeMillis()) {
            throw new RuntimeException("token失效，请重新登录");
        }

        request.setAttribute("userId", tokenEntity.getUserid());
        request.setAttribute("role", tokenEntity.getRole());
        request.setAttribute("tableName", tokenEntity.getTablename());
        return true;
    }
}
