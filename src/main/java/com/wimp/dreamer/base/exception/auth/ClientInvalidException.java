package com.wimp.dreamer.base.exception.auth;


import com.wimp.dreamer.base.exception.BaseException;
import com.wimp.dreamer.base.exception.constant.ExceptionConstant;

/**
 * @author zy
 * 客户端非法
 */
public class ClientInvalidException extends BaseException {
    public ClientInvalidException(String message) {
        super(message, ExceptionConstant.EX_CLIENT_INVALID_CODE);
    }
}
