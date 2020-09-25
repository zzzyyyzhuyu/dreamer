package com.wimp.dreamer.security.auth.authorization.config;

import com.wimp.dreamer.security.auth.authorization.handler.DreamerLogoutSuccessHandler;
import com.wimp.dreamer.security.auth.authorization.properties.OAuth2Properties;
import com.wimp.dreamer.security.auth.authorization.token.JwtTokenEnhancer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;

/**
 * @author zy
 * @date 2020/9/25
 * <p>
 *  
 */
@Configuration
public class AuthorizationBeanConfiguration {
    @Resource
    private OAuth2Properties oAuth2Properties;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * Jwt access token converter jwt access token converter.
     *
     * @return the jwt access token converter
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(oAuth2Properties.getJwtSigningKey());
        return jwtAccessTokenConverter;
    }

    /**
     * Jwt token store token store.
     * 延迟注册
     * @return the token store
     */
    @Bean
    @DependsOn("jwtAccessTokenConverter")
    public TokenStore jwtTokenStore(@Qualifier("jwtAccessTokenConverter") JwtAccessTokenConverter jwtAccessTokenConverter ) {
        return new JwtTokenStore(jwtAccessTokenConverter);

    }

    /**
     * Jwt token enhancer token enhancer.
     *
     * @return the token enhancer
     */
    @Bean
    @ConditionalOnBean(TokenEnhancer.class)
    public TokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }



    /**
     * 退出时的处理策略配置
     *
     * @return logout success handler
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new DreamerLogoutSuccessHandler();
    }
}
