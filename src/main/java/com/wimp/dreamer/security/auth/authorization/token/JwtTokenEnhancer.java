package com.wimp.dreamer.security.auth.authorization.token;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @date 2020/9/18
 * <p>
 * JwtTokenEnhancer 向jwtToken中加入自定义信息
 */
public class JwtTokenEnhancer implements TokenEnhancer {


    /**
     * token中增加附加信息
     * @param accessToken          the access token
     * @param oAuth2Authentication the o auth 2 authentication
     *
     * @return the o auth 2 access token
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<>(8);
        info.put("timestamp", System.currentTimeMillis());
        Authentication authentication = oAuth2Authentication.getUserAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            Object principal = authentication.getPrincipal();
            info.put("loginName", ((UserDetails) principal).getUsername());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
