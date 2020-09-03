package com.wimp.dreamer.base.exception.auth;


import com.wimp.dreamer.base.exception.BaseException;
import com.wimp.dreamer.base.exception.constant.ExceptionConstant;

/**
 * @author zy
 * 客户端token异常
 */
public class ClientTokenException extends BaseException {
    public ClientTokenException(String message) {
        super(message, ExceptionConstant.EX_CLIENT_INVALID_CODE);
    }
}
