package com.wimp.dreamer.security.auth.authorization.provider;

import com.wimp.dreamer.security.auth.authorization.properties.OAuth2Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  OAuth2客户端信息获取 
 */
@Component("restClientDetailsService")
@Slf4j
public class RestClientDetailsServiceImpl implements ClientDetailsService {
    private ClientDetailsService clientDetailsService;

    @Resource
    private OAuth2Properties oAuth2Properties;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        InMemoryClientDetailsServiceBuilder builder = new InMemoryClientDetailsServiceBuilder();
        if (ArrayUtils.isNotEmpty(oAuth2Properties.getOauth2().getClientIds())) {
            for (String clientId : oAuth2Properties.getOauth2().getClientIds()) {
                builder.withClient(clientId)
                        .secret(oAuth2Properties.getOauth2().getClientSecret())
                        .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                        .accessTokenValiditySeconds(oAuth2Properties.getOauth2().getAccessTokenValidateSeconds())
                        .refreshTokenValiditySeconds(oAuth2Properties.getOauth2().getRefreshTokenValiditySeconds())
                        .scopes(oAuth2Properties.getOauth2().getScope());
            }
        }
        try {
            clientDetailsService = builder.build();
        } catch (Exception e) {
            log.error("init={}", e.getMessage(), e);
        }
    }

    /**
     * 获取OAuth2客户端信息
     * @param clientId 客户端id
     * @return
     * @throws ClientRegistrationException
     */
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        return clientDetailsService.loadClientByClientId(clientId);
    }
}
