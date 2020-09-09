package com.wimp.dreamer.security.auth.authentication.provider;

import com.wimp.dreamer.security.auth.authentication.token.ThirdAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zy
 * @date 2020/9/9
 * <p>
 * 第三方认证服务提供者
 */
@Getter
@Setter
public class ThirdAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    /**
     * 自定义的三方认证方法，在调用之前，通过ThirdAuthenticationFilter过滤器放入token
     * @param authentication
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        ThirdAuthenticationToken authenticationToken = (ThirdAuthenticationToken) authentication;
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        BCryptPasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();
        if(StringUtils.isBlank(authenticationToken.getPwd())){
            throw new BadCredentialsException("密码 为空!");
        }
        if(!passwordEncoder.matches(authenticationToken.getPwd(),user.getPassword())){
            throw new BadCredentialsException("用户名或密码错误");
        }
        ThirdAuthenticationToken authenticationResult = new ThirdAuthenticationToken(user,"", user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());
        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ThirdAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
