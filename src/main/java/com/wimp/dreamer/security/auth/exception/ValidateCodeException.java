package com.wimp.dreamer.security.auth.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常
 * @author zy
 */
public class ValidateCodeException extends AuthenticationException {


	private static final long serialVersionUID = -7285211528095468156L;


	public ValidateCodeException(String msg) {
		super(msg);
	}

}
