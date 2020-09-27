package com.wimp.dreamer.security.auth.authorization.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimp.dreamer.base.msg.BaseResponse;
import com.wimp.dreamer.base.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
@Component
public class DreamerLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private ConsumerTokenServices consumerTokenServices;

    @Resource
    private RedisUtil redisUtil;

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
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = StringUtils.substringAfter(authorization, "Bearer ");
        consumerTokenServices.revokeToken(token);
        redisUtil.expire(RedisUtil.ACCESS_TOKEN_KEY + token,1);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse("退出成功")));
    }
}
