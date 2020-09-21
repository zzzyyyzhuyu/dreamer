package com.wimp.dreamer.security.auth.authentication.config;

import com.wimp.dreamer.security.auth.authentication.filter.ThirdAuthenticationFilter;
import com.wimp.dreamer.security.auth.authentication.provider.ThirdAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author zy
 * @date 2020/9/9
 * <p>
 * 第三方三方登录配置（自定义）
 */
@Component
public class ThirdAuthenticationConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Resource
    private AuthenticationSuccessHandler pcAuthenticationSuccessHandler;
    @Resource
    private AuthenticationFailureHandler pcAuthenticationFailureHandler;
    @Resource
    private UserDetailsService userDetailsService;
    @Resource
    private PersistentTokenRepository persistentTokenRepository;

    @Override
    public void configure(HttpSecurity http) {
        ThirdAuthenticationFilter authenticationFilter = new ThirdAuthenticationFilter();
        authenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        authenticationFilter.setAuthenticationSuccessHandler(pcAuthenticationSuccessHandler);
        authenticationFilter.setAuthenticationFailureHandler(pcAuthenticationFailureHandler);
        String key = UUID.randomUUID().toString();
        authenticationFilter.setRememberMeServices(new PersistentTokenBasedRememberMeServices(key, userDetailsService, persistentTokenRepository));
        ThirdAuthenticationProvider thirdAuthenticationProvider = new ThirdAuthenticationProvider();
        thirdAuthenticationProvider.setUserDetailsService(userDetailsService);
        http.authenticationProvider(thirdAuthenticationProvider)
                .addFilterAfter(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
