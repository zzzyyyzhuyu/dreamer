package com.wimp.dreamer.security.auth.authentication.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimp.dreamer.base.msg.BaseResponse;
import com.wimp.dreamer.security.auth.exception.UserDisabledException;
import com.wimp.dreamer.security.auth.exception.ValidateCodeException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zy
 * @date 2020/9/9
 * <p>
 *  认证失败handler
 */
@Component("pcAuthenticationFailureHandler")
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 重写认证失败方法
     * 认证失败，返回json数据
     * @param request
     * @param response
     * @param exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        //返回异常信息
        if(exception instanceof BadCredentialsException || exception.getCause() instanceof BadCredentialsException){
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(4001,exception.getMessage())));
        }else if(exception instanceof ValidateCodeException || exception.getCause() instanceof ValidateCodeException){
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(4002,exception.getMessage())));
        }else if(exception instanceof UserDisabledException || exception.getCause() instanceof UserDisabledException){
            response.setStatus(HttpStatus.OK.value());
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(4005,exception.getMessage())));
        } else{
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.getWriter().write(objectMapper.writeValueAsString(new BaseResponse(500,exception.getMessage())));
        }
    }
}
