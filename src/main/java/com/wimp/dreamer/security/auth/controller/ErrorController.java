package com.wimp.dreamer.security.auth.controller;

import org.apache.http.HttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zy
 * @date 2020/9/24
 * <p>
 *
 */
@RestController
public class ErrorController {
    @GetMapping("/error")
    public void error(HttpRequest request, HttpResponse response){
        request.getHeaders();
        response.getEntity();
    }
}
