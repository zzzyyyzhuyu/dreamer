package com.wimp.dreamer.base.exception;

import com.wimp.dreamer.base.exception.enums.ErrorCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zy
 * @date 2020/9/3
 * <p>
 * 基础异常,后续自定义异常应基于此类
 */
@Getter
@Setter
public class BaseException extends RuntimeException {
    private int status = 200;


    public BaseException() {
    }

    public BaseException(String message, int status) {
        super(message);
        this.status = status;
    }

    public BaseException(ErrorCode codeEnum, Object... args) {
        super(String.format(codeEnum.msg(), args));
        this.status = codeEnum.code();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
