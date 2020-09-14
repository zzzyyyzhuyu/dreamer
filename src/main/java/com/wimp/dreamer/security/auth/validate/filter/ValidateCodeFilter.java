package com.wimp.dreamer.security.auth.validate.filter;

import com.wimp.dreamer.security.auth.constant.SecurityConstants;
import com.wimp.dreamer.security.auth.exception.ValidateCodeException;
import com.wimp.dreamer.security.auth.validate.enums.ValidateCodeType;
import com.wimp.dreamer.security.auth.validate.holder.ValidateCodeProcessorHolder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author zy
 * @date 2020/9/10
 * <p>
 * 校验码过滤器 
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    /**
     * 验证码校验失败处理器
     */
    @Resource
    private AuthenticationFailureHandler authenticationFailureHandler;
    /**
     * 系统中的校验码处理器
     */
    @Resource
    private ValidateCodeProcessorHolder validateCodeProcessorHolder;
    /**
     * 存放所有需要校验验证码的url
     */
    private Map<String, ValidateCodeType> urlMap = new HashMap<>();
    /**
     * 验证请求url与配置的url是否匹配的工具类
     */
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final String GET = "get";

    /**
     * 初始化要拦截的url配置信息
     * 初始化参数，form类型配置图片验证码，可进行扩展
     *
     * @throws ServletException the servlet exception
     */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        urlMap.put(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM, ValidateCodeType.IMAGE);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        ValidateCodeType type = getValidateCodeType(httpServletRequest);
        if (type != null) {
            try {
                //获取校验处理器进行处理
                validateCodeProcessorHolder.findValidateCodeProcessor(type).validate(new ServletWebRequest(httpServletRequest, httpServletResponse));
            } catch (ValidateCodeException exception) {
                //校验异常，则认证失败处理
                authenticationFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, exception);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * 获取验证类型
     *
     * @param request 请求
     * @return ValidateCodeType
     */
    private ValidateCodeType getValidateCodeType(HttpServletRequest request) {
        ValidateCodeType result = null;
        if (!StringUtils.equalsIgnoreCase(request.getMethod(), GET)) {
            Set<String> urls = urlMap.keySet();
            for (String url : urls) {
                if (pathMatcher.match(url, request.getRequestURI())) {
                    result = urlMap.get(url);
                }
            }
        }
        return result;
    }
}
