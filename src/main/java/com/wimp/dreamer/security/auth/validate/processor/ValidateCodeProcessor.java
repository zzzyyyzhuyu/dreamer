package com.wimp.dreamer.security.auth.validate.processor;

import com.wimp.dreamer.security.auth.validate.enums.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author zy
 * @date 2020/9/11
 * <p>
 *  验证码处理器
 */
public interface ValidateCodeProcessor {

    /**
     * 获取处理器类型
     * @return ValidateCodeType
     */
    ValidateCodeType getValidateCodeType();

    /**
     * 校验方法
     * @param servletWebRequest 请求
     */
    void validate(ServletWebRequest servletWebRequest);
}
