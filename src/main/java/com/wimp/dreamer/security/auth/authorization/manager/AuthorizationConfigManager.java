package com.wimp.dreamer.security.auth.authorization.manager;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * @author zy
 * @date 2020/9/16
 * <p>
 *  授权信息管理器
 *  用于收集系统中所有 AuthorizationConfigProvider 并加载其配置
 */
public interface AuthorizationConfigManager {
    /**
     * Config.
     *
     * @param config the config
     */
    void config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config);

}
