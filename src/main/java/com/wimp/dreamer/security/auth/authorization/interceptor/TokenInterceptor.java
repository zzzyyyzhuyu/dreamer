package com.wimp.dreamer.security.auth.authorization.interceptor;

import com.wimp.dreamer.base.exception.auth.ClientTokenException;
import com.wimp.dreamer.base.exception.enums.ErrorCode;
import com.wimp.dreamer.base.utils.RedisUtil;
import com.wimp.dreamer.base.utils.ThreadLocalMap;
import com.wimp.dreamer.security.auth.constant.GlobalConstant;
import com.wimp.dreamer.security.auth.dto.LoginAuthDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zy
 * @date 2020/9/15
 * <p>
 * token拦截器，旨在放入全局用户信息
 */
@Component
@Slf4j
public class TokenInterceptor implements HandlerInterceptor {


    @Resource
    private RedisUtil redisUtil;

    private static final String OPTIONS = "OPTIONS";
    private static final String AUTH_PATH1 = "/auth";
    private static final String AUTH_PATH2 = "/oauth";
    private static final String AUTH_PATH3 = "/error";
    private static final String AUTH_PATH4 = "/api";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String uri = request.getRequestURI();
        if (uri.contains(AUTH_PATH1) || uri.contains(AUTH_PATH2) || uri.contains(AUTH_PATH3) || uri.contains(AUTH_PATH4)) {
            //认证、授权、错误、开放接口不拦截
            return true;
        }
        if (OPTIONS.equalsIgnoreCase(request.getMethod())) {
            //文件上传请求不拦截，在文件上传前，会发送options方式请求
            return true;
        }
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(StringUtils.isBlank(authorization) || !authorization.contains("Bearer")){
            return true;
        }
        /**
         * jwt类型的token本身无状态，无法根据token判断用户是否退出登录，此处增加redis进行登录用户校验
         * 登录用户不存在时，即用户注销
         */
        String token = StringUtils.substringAfter(authorization, "Bearer ");
        LoginAuthDto loginUser = (LoginAuthDto) redisUtil.get(RedisUtil.ACCESS_TOKEN_KEY+token);
        if (loginUser == null) {
            throw new ClientTokenException("token失效");
        }
        ThreadLocalMap.put(GlobalConstant.Sys.TOKEN_AUTH_DTO, loginUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(ex != null){
            log.info("tokenInterceptor Exception",ex);
            this.handleException(response,ex);
        }
    }

    /**
     * token异常处理
     * @param response 返回请求
     * @param ex 异常信息
     */
    private void handleException(HttpServletResponse response, Exception ex) throws IOException {
        response.resetBuffer();
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"code\":"+ ErrorCode.UAC10010002.code() +" ,\"message\" :\""+ex.getMessage()+"\"}");
        response.flushBuffer();
    }
}
