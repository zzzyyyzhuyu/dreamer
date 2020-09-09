package com.wimp.dreamer.security.auth.authentication.filter;

import com.wimp.dreamer.security.auth.authentication.token.ThirdAuthenticationToken;
import com.wimp.dreamer.security.auth.constant.SecurityConstants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zy
 * @date 2020/9/9
 * <p>
 * 自定义过滤器 
 */
@Getter
@Setter
public class ThirdAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String POST = "POST";

    private String parameterUserName = "username";
    private String parameterPassword = "password";
    private boolean postOnly = true;

    public ThirdAuthenticationFilter() {
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_TPOS, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (postOnly && !POST.equals(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        String userName = obtainUserName(request);
        if (userName == null) {
            userName = "";
        }
        userName = userName.trim();
        ThirdAuthenticationToken authRequestToken = new ThirdAuthenticationToken(userName,obtainPwd(request));
        setDetails(request, authRequestToken);
        return this.getAuthenticationManager().authenticate(authRequestToken);
    }


    /**
     * 获取用户名
     * @param request 请求request
     * @return 用户名
     */
    private String obtainUserName(HttpServletRequest request) {
        return request.getParameter(parameterUserName);
    }

    /**
     * 获取密码
     * @param request 请求request
     * @return 密码（md5加密后）
     */
    private String obtainPwd(HttpServletRequest request){
        return request.getParameter(parameterPassword);
    }

    private void setDetails(HttpServletRequest request, ThirdAuthenticationToken authRequestToken ) {
        authRequestToken.setDetails(authenticationDetailsSource.buildDetails(request));
    }

}
