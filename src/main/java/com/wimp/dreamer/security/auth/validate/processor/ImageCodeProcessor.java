package com.wimp.dreamer.security.auth.validate.processor;

import com.wimp.dreamer.base.utils.RedisUtil;
import com.wimp.dreamer.security.auth.constant.SecurityConstants;
import com.wimp.dreamer.security.auth.exception.ValidateCodeException;
import com.wimp.dreamer.security.auth.validate.enums.ValidateCodeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;

/**
 * @author zy
 * @date 2020/9/11
 * <p>
 *  图片验证码类型处理器
 */
public class ImageCodeProcessor implements ValidateCodeProcessor {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public ValidateCodeType getValidateCodeType() {
        return ValidateCodeType.IMAGE;
    }

    @Override
    public void validate(ServletWebRequest request) {
        ValidateCodeType codeType = getValidateCodeType();
        String codeInRequest;
        String codeId;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), codeType.getParamNameOnValidate());
            codeId = ServletRequestUtils.getStringParameter(request.getRequest(),"codeId");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if (StringUtils.isBlank(codeInRequest) && StringUtils.isBlank(codeId)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }
        String redisCode = (String)redisUtil.get(SecurityConstants.IMAGE_CODE_PREFIX+codeId);
        if(StringUtils.isBlank(redisCode)){
            throw new ValidateCodeException("验证码已过期");
        }
        assert codeInRequest != null;
        if (!codeInRequest.equalsIgnoreCase(redisCode)) {
            throw new ValidateCodeException("验证码不正确");
        }
    }
}
