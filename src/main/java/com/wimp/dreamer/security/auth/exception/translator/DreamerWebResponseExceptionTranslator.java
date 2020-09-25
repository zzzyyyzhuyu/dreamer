package com.wimp.dreamer.security.auth.exception.translator;

import com.wimp.dreamer.base.msg.BaseResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

/**
 * @author zy
 * @date 2020/9/25
 * <p>
 * 异常返回值封装
 */
@Log4j2
@Component
public class DreamerWebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception e) {
        log.error("OAuh2 异常："+e);
        BaseResponse baseResponse = new BaseResponse(HttpStatus.UNAUTHORIZED.value(),e.getMessage());
        return new ResponseEntity(baseResponse,HttpStatus.UNAUTHORIZED);
    }
}
