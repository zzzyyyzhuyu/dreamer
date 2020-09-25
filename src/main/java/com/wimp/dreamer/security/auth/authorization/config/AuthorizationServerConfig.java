package com.wimp.dreamer.security.auth.authorization.config;

import com.wimp.dreamer.security.auth.authorization.handler.DreamerLogoutSuccessHandler;
import com.wimp.dreamer.security.auth.authorization.properties.OAuth2Properties;
import com.wimp.dreamer.security.auth.authorization.provider.RestClientDetailsServiceImpl;
import com.wimp.dreamer.security.auth.authorization.token.JwtTokenEnhancer;
import com.wimp.dreamer.security.auth.exception.translator.DreamerWebResponseExceptionTranslator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  授权服务器配置
 *  bean注入顺序问题（同一个类内，@Resource可能不生效）
 */
@Component
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Resource
    private OAuth2Properties oAuth2Properties;

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private RestClientDetailsServiceImpl restClientDetailsService;

    @Resource
    private DreamerWebResponseExceptionTranslator dreamerWebResponseExceptionTranslator;


    private JwtAccessTokenConverter jwtAccessTokenConverter;

    private TokenStore tokenStore;

    private TokenEnhancer jwtTokenEnhancer;

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
        jwtAccessTokenConverter = new JwtAccessTokenConverter();
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
    public TokenStore jwtTokenStore() {
        tokenStore =  new JwtTokenStore(jwtAccessTokenConverter);
        return tokenStore;
    }

    /**
     * Jwt token enhancer token enhancer.
     *
     * @return the token enhancer
     */
    @Bean
    @ConditionalOnBean(TokenEnhancer.class)
    public TokenEnhancer jwtTokenEnhancer() {
        jwtTokenEnhancer = new JwtTokenEnhancer();
        return jwtTokenEnhancer;
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


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(restClientDetailsService);
    }

    /**
     * Configure.
     *
     * @param security the security
     *
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) {
        security.tokenKeyAccess("permitAll()").allowFormAuthenticationForClients();
    }

    /**
     * Configure.
     *
     * @param endpoints the endpoints
     *
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);

        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> enhancers = new ArrayList<>();
        enhancers.add(jwtTokenEnhancer);
        enhancers.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(enhancers);
        endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        endpoints.exceptionTranslator(dreamerWebResponseExceptionTranslator);
    }


}
