package com.wimp.dreamer.provider.test.conrtroller;

import com.wimp.dreamer.base.msg.ObjectRestResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zy
 * @date 2020/9/23
 * <p>
 *  token权限测试Controller
 */
@RestController
@RequestMapping("/token")
public class TokenTestController {

    @GetMapping("/test")
    public ObjectRestResponse test(){
        return ObjectRestResponse.ok("请求成功");
    }
}
