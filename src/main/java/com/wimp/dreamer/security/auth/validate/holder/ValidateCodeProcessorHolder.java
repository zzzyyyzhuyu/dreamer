package com.wimp.dreamer.security.auth.validate.holder;

import com.wimp.dreamer.security.auth.exception.ValidateCodeException;
import com.wimp.dreamer.security.auth.validate.enums.ValidateCodeType;
import com.wimp.dreamer.security.auth.validate.processor.ValidateCodeProcessor;
import com.wimp.dreamer.security.auth.validate.util.SpringContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zy
 * @date 2020/9/10
 * <p>
 * 校验处理器中心 
 */
@Component("validateCodeProcessorHolder")
public class ValidateCodeProcessorHolder {

    @Resource
    private SpringContextAware springContextAware;

    private Map<String, ValidateCodeProcessor> validateCodeProcessors = null;

    /**
     * Find validate code processor validate code processor.
     *
     * @param type the type
     * @return validate code processor
     */
    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.getParamNameOnValidate());
    }

    /**
     * @param type the type
     * @return validate code processor
     */
    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        //延迟加载认证器
        if (this.validateCodeProcessors == null) {
            synchronized (this) {
                validateCodeProcessors = new HashMap<>();
                Map<String, ValidateCodeProcessor> validateCodeProcessorsMap = springContextAware.getApplicationContext().getBeansOfType(ValidateCodeProcessor.class);
                if (validateCodeProcessorsMap != null) {
                    for (ValidateCodeProcessor cp : validateCodeProcessorsMap.values()) {
                        validateCodeProcessors.put(cp.getValidateCodeType().getParamNameOnValidate(), cp);
                    }
                }
            }
        }

        ValidateCodeProcessor processor = validateCodeProcessors.get(type);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + type + "不存在");
        }
        return processor;
    }
}
