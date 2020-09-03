package com.wimp.dreamer.base.exception.auth;


import com.wimp.dreamer.base.exception.BaseException;
import com.wimp.dreamer.base.exception.constant.ExceptionConstant;

/**
 * @author zy
 * 用户token异常
 */
public class UserTokenException extends BaseException {
    public UserTokenException(String message) {
        super(message, ExceptionConstant.EX_USER_TOKEN_INVALID_CODE);
    }
}
