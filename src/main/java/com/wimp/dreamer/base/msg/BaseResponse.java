package com.wimp.dreamer.base.msg;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zy
 * @date 2020/9/3
 * <p>
 *  基础返回值
 */
@Getter
@Setter
public class BaseResponse {
    private int status = 200;
    private String message;
    private Date timeStamp;

    public BaseResponse() {
        this.timeStamp = new Date();
    }

    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.timeStamp = new Date();
    }
    public BaseResponse(String message) {
        this.message = message;
        this.timeStamp = new Date();
    }


}
