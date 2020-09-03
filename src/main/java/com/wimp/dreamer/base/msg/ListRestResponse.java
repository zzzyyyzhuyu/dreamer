package com.wimp.dreamer.base.msg;


import lombok.Getter;
import lombok.Setter;

/**
 * @author zhuyu
 * list类型返回值
 */
@Setter
@Getter
public class ListRestResponse<T> {

    private String msg;
    private T result;
    private int count;

    public ListRestResponse<T> count(int count) {
        this.setCount(count);
        return this;
    }

    public ListRestResponse<T> count(Long count) {
        this.setCount(count.intValue());
        return this;
    }

    public ListRestResponse<T> msg(String msg) {
        this.setMsg(msg);
        return this;
    }

    public ListRestResponse<T> result(T result) {
        this.setResult(result);
        return this;
    }

}
