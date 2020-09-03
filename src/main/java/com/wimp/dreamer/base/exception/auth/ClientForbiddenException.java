package com.wimp.dreamer.base.exception.auth;


import com.wimp.dreamer.base.exception.BaseException;
import com.wimp.dreamer.base.exception.constant.ExceptionConstant;

/**
 * @author zy
 * 客户端被禁止
 */
public class ClientForbiddenException extends BaseException {
    public ClientForbiddenException(String message) {
        super(message, ExceptionConstant.EX_CLIENT_FORBIDDEN_CODE);
    }

}
