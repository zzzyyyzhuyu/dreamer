package com.wimp.dreamer.security.auth.authorization.properties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  Oauth2客户端配置类
 */
@Getter
@Setter
public class OAuth2ClientProperties {
    /**
     * 第三方应用appSecret
     */
    private String clientSecret;
    /**
     * token有效期
     */
    private int accessTokenValidateSeconds = 7200;
    /**
     * refreshToken有效期
     */
    private int refreshTokenValiditySeconds = 2592000;

    private String scope = "*";
    /**
     * 客户端id
     */
    private String[] clientIds = {};
}
