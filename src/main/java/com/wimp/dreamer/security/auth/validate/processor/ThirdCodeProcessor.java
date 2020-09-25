package com.wimp.dreamer.security.auth.validate.processor;

import com.wimp.dreamer.security.auth.validate.enums.ValidateCodeType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author zy
 * @date 2020/9/14
 * <p>
 *  三方校验处理器
 */
@Component("thirdCodeProcessor")
public class ThirdCodeProcessor implements ValidateCodeProcessor {

    @Override
    public ValidateCodeType getValidateCodeType() {
        return ValidateCodeType.THIRD;
    }

    @Override
    public void validate(ServletWebRequest servletWebRequest) {
        //TODO 扩展你想要的验证方式
    }
}
