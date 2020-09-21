package com.wimp.dreamer.security.auth.authorization.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimp.dreamer.base.msg.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zy
 * @date 2020/9/18
 * <p>
 * 退出登录成功时的处理器 
 */
@Slf4j
public class DreamerLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * On logout success.
     *
     * @param request        the request
     * @param response       the response
     * @param authentication the authentication
     *
     * @throws IOException the io exception
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        log.info("退出成功");
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse("退出成功")));
    }
}
