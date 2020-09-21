package com.wimp.dreamer.security.auth.authorization.provider;

import com.wimp.dreamer.security.auth.constant.SecurityConstants;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  PcAuthorizationConfigProvider
 * 核心模块的授权配置提供者，安全模块涉及的url的授权配置在这里。
 */
@Component
public class PcAuthorizationConfigProvider implements AuthorizationConfigProvider {

    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        config.antMatchers(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_THIRD,
                SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*", "/health","/health/**",
                "/druid/**", "/auth/**", "/swagger-ui.html", "/swagger-resources/**","/api/applications" ,"/v2/api-docs").permitAll();
        return false;
    }
}
