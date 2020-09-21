package com.wimp.dreamer.security.auth.authorization.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * AuthAccessDeniedHandler
 * 授权失败处理器
 *
 * @author zhuyu
 */
@Configuration
@Slf4j
public class AuthAccessDeniedHandler implements AccessDeniedHandler {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * Handle.
     *
     * @param request  the request
     * @param response the response
     * @param e        the e
     * @throws JsonProcessingException the json processing exception
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
        log.info("处理权限异常:", e);
        Map<String, Object> result = new HashMap<>(3);
        result.put("code", 99990401);
        result.put("message", "无访问权限");
        String json = objectMapper.writeValueAsString(result);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }
}