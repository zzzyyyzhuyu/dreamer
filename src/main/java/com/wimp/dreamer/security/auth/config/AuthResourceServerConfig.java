package com.wimp.dreamer.security.auth.config;

import com.wimp.dreamer.security.auth.authentication.config.FormAuthenticationConfig;
import com.wimp.dreamer.security.auth.authentication.config.ThirdAuthenticationConfig;
import com.wimp.dreamer.security.auth.authorization.manager.AuthorizationConfigManager;
import com.wimp.dreamer.security.auth.validate.config.ValidateCodeSecurityConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * @author zy
 * @date 2020/9/18
 * <p>
 * 资源服务器配置
 */
@Component
@EnableResourceServer
public class AuthResourceServerConfig implements ResourceServerConfigurer {

    @Resource
    private OAuth2WebSecurityExpressionHandler pcSecurityExpressionHandler;

    @Resource
    private AccessDeniedHandler pcAccessDeniedHandler;

    @Resource
    protected AuthenticationSuccessHandler pcAuthenticationSuccessHandler;

    @Resource
    protected AuthenticationFailureHandler pcAuthenticationFailureHandler;

    @Resource
    private AuthorizationConfigManager authorizationConfigManager;

    @Resource
    private FormAuthenticationConfig formAuthenticationConfig;

    @Resource
    private ThirdAuthenticationConfig thirdAuthenticationConfig;

    @Resource
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Resource
    private DataSource dataSource;

    /**
     * 记住我功能的token存取器配置
     *
     * @return the persistent token repository
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer configurer) throws Exception {
        configurer.expressionHandler(pcSecurityExpressionHandler);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        formAuthenticationConfig.configure(http);
        http.headers().frameOptions().disable();
        http.apply(validateCodeSecurityConfig)
                .and().apply(thirdAuthenticationConfig).and()
                .exceptionHandling().accessDeniedHandler(pcAccessDeniedHandler)
                .and()
                .csrf().disable();

        authorizationConfigManager.config(http.authorizeRequests());
    }
}
