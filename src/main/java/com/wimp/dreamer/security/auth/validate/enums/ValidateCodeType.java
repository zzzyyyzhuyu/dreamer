package com.wimp.dreamer.security.auth.validate.enums;

import com.wimp.dreamer.security.auth.constant.SecurityConstants;

/**
 * @author zy
 * @date 2020/9/10
 * <p>
 *  校验类型枚举
 */
public enum ValidateCodeType {

    /**
     * 图片验证码
     */
    IMAGE {
        @Override
        public String getParamNameOnValidate() {
            return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
        }
    },
    /**
     * 三方登录，可扩展
     */
    THIRD {
        @Override
        public String getParamNameOnValidate() {
            return "THIRD";
        }
    }
    ;
    public abstract String getParamNameOnValidate();
}
