package com.wimp.dreamer.security.auth.authorization.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  
 */

@ConfigurationProperties(prefix = "dreamer.oauth2")
@Getter
@Setter
public class OAuth2Properties {
    /**
     * 使用jwt时为token签名的秘钥
     */
    private String jwtSigningKey;

    private String tokenRefreshUrl;
    /**
     * 客户端配置
     */
    private OAuth2ClientProperties oauth2 = new OAuth2ClientProperties();


}


