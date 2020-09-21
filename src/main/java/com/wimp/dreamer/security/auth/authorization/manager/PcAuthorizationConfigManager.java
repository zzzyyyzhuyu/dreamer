package com.wimp.dreamer.security.auth.authorization.manager;

import com.wimp.dreamer.security.auth.authorization.provider.AuthorizationConfigProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  PcAuthorizationConfigManager
 */
@Component
public class PcAuthorizationConfigManager implements AuthorizationConfigManager {

    private final List<AuthorizationConfigProvider> authorizationConfigProviders;

    /**
     * Instantiates a new Pc authorization config manager.
     *
     * @param authorizationConfigProviders the authorization config providers
     */

    @Autowired
    public PcAuthorizationConfigManager(List<AuthorizationConfigProvider> authorizationConfigProviders) {
        this.authorizationConfigProviders = authorizationConfigProviders;
    }

    @Override
    public void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        boolean existAnyRequestConfig = false;
        String existAnyRequestConfigName = null;

        for (AuthorizationConfigProvider authorizationConfigProvider : authorizationConfigProviders) {
            boolean currentIsAnyRequestConfig = authorizationConfigProvider.config(config);
            if (existAnyRequestConfig && currentIsAnyRequestConfig) {
                throw new RuntimeException("重复的anyRequest配置:" + existAnyRequestConfigName + ","
                        + authorizationConfigProvider.getClass().getSimpleName());
            } else if (currentIsAnyRequestConfig) {
                existAnyRequestConfig = true;
                existAnyRequestConfigName = authorizationConfigProvider.getClass().getSimpleName();
            }
        }
        if (!existAnyRequestConfig) {
            //不需要授权接口,也不需要进行认证
            config.anyRequest().authenticated();
        }
    }
}
