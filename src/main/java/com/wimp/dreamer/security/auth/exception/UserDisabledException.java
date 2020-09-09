package com.wimp.dreamer.security.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 用户禁用异常
 * @author zy
 */
public class UserDisabledException extends AuthenticationException {

    public UserDisabledException(String msg) {
        super(msg);
    }



}
