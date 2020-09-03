package com.wimp.dreamer.base.exception.auth;


import com.wimp.dreamer.base.exception.BaseException;
import com.wimp.dreamer.base.exception.constant.ExceptionConstant;

/**
 * @author zy
 * 用户非法
 */
public class UserInvalidException extends BaseException {
    public UserInvalidException(String message) {
        super(message, ExceptionConstant.EX_USER_INVALID_CODE);
    }
}
