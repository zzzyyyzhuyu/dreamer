package com.wimp.dreamer.base.msg;

import lombok.Getter;
import lombok.Setter;

/**
 * @param <T>
 * @author zy
 * Object类型返回值
 */
@Getter
@Setter
public class ObjectRestResponse<T> extends BaseResponse {

    private T data;
    private boolean rel = true;

    public ObjectRestResponse() {
    }

    public static <E> ObjectRestResponse<E> ok(E o) {
        return new ObjectRestResponse<>(200, "操作成功", o, true);
    }

    public static <E> ObjectRestResponse<E> error() {
        return new ObjectRestResponse<>(500, "操作失败", null, false);
    }


    public ObjectRestResponse(int status, String message, T data, boolean rel) {
        super(status, message);
        this.data = data;
        this.rel = rel;
    }


    public ObjectRestResponse<T> rel(boolean rel) {
        this.setRel(rel);
        return this;
    }


    public ObjectRestResponse<T> data(T data) {
        this.setData(data);
        return this;
    }


}
