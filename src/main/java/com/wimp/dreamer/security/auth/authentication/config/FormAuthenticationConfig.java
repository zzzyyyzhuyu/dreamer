package com.wimp.dreamer.security.auth.authentication.config;

import com.wimp.dreamer.security.auth.constant.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


/**
 * @author zy
 * @date 2020/9/8
 * <p>
 *  表单验证认证配置
 */
@Component
public class FormAuthenticationConfig {


    /**
     * The Pc authentication success handler.
     * 认证成功handler
     */
    protected final AuthenticationSuccessHandler pcAuthenticationSuccessHandler;

    /**
     * The Pc authentication failure handler.
     * 认证失败handler
     *
     */
    protected final AuthenticationFailureHandler pcAuthenticationFailureHandler;

    /**
     * Instantiates a new Form authentication config.
     *
     * @param pcAuthenticationSuccessHandler the pc authentication success handler
     * @param pcAuthenticationFailureHandler the pc authentication failure handler
     */
    @Autowired
    public FormAuthenticationConfig(AuthenticationSuccessHandler pcAuthenticationSuccessHandler, AuthenticationFailureHandler pcAuthenticationFailureHandler) {
        this.pcAuthenticationSuccessHandler = pcAuthenticationSuccessHandler;
        this.pcAuthenticationFailureHandler = pcAuthenticationFailureHandler;
    }

    /**
     * Configure.
     * 配置
     * @param http the http
     *
     * @throws Exception the exception
     */
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_FORM)
                .successHandler(pcAuthenticationSuccessHandler)
                .failureHandler(pcAuthenticationFailureHandler);
    }
}
