package com.wimp.dreamer.security.auth.authentication.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.util.Collection;

/**
 * @author zy
 * @date 2020/9/9
 * <p>
 * 自定义认证token
 */
@Getter
@Setter
public class ThirdAuthenticationToken extends AbstractAuthenticationToken
        implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Object principal;
    private final String pwd;

    /**
     * Creates a token with the supplied array of authorities.
     *
     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
     *                    represented by this authentication object.
     * @param principal
     * @param pwd
     */
    public ThirdAuthenticationToken(Object principal, String pwd,Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.pwd = pwd;
        super.setAuthenticated(true);
    }

    /**
     * 用于ThirdAuthenticationFilter中，增加认证token，默认不可信
     * @param principal 凭证（此处用用户名）
     * @param pwd 密码
     */
    public ThirdAuthenticationToken(Object principal,String pwd){
        super(null);
        this.principal = principal;
        this.pwd = pwd;
        setAuthenticated(false);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }

        super.setAuthenticated(false);
    }
}
