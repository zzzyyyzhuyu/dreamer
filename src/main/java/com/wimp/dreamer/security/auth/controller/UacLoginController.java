package com.wimp.dreamer.security.auth.controller;


import com.google.code.kaptcha.Producer;
import com.wimp.dreamer.base.msg.ObjectRestResponse;
import com.wimp.dreamer.base.utils.RedisUtil;
import com.wimp.dreamer.base.utils.RequestUtil;
import com.wimp.dreamer.base.web.controller.BaseController;
import com.wimp.dreamer.security.auth.biz.UserBiz;
import com.wimp.dreamer.security.auth.constant.SecurityConstants;
import com.wimp.dreamer.security.auth.domain.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;

/**
 * 登录Token相关
 *
 * @author zy
 */
@RestController
@RequestMapping
public class UacLoginController extends BaseController<UserBiz, User> {

    @Resource
    private Producer producer;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private ClientDetailsService clientDetailsService;
    private static final String BEARER_TOKEN_TYPE = "Basic ";

    /**
     * 刷新token.
     *
     * @param request      the request
     * @param refreshToken the refresh token
     * @param accessToken  the access token
     * @return the wrapper
     */
    @GetMapping(value = "/auth/user/refreshToken")
    public ObjectRestResponse<String> refreshToken(HttpServletRequest request, @RequestParam(value = "refreshToken") String refreshToken, @RequestParam(value = "accessToken") String accessToken) {
        String token;
        try {
            String header = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (header == null || !header.startsWith(BEARER_TOKEN_TYPE)) {
                throw new UnapprovedClientAuthenticationException("请求头中无client信息");
            }
            String[] tokens = RequestUtil.extractAndDecodeHeader(header);
            assert tokens.length == 2;

            String clientId = tokens[0];
            String clientSecret = tokens[1];

            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

            if (clientDetails == null) {
                throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
            } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
                throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
            }

            token = this.baseBiz.refreshToken(accessToken, refreshToken, request);
        } catch (Exception e) {
            logger.error("refreshToken={}", e.getMessage(), e);
            return new ObjectRestResponse<>(403, "", "", false);
        }
        return ObjectRestResponse.ok(token);
    }

    @RequestMapping(value = "/auth/user/getVcode")
    public void getValidateCode(HttpServletRequest req, HttpServletResponse rsp, @RequestParam String vcodeId) throws Exception {

        rsp.setDateHeader("Expires", 0);
        rsp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        rsp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        rsp.setHeader("Pragma", "no-cache");
        rsp.setContentType("image/jpeg");
        String capText = producer.createText();
        try {
            redisUtil.set(SecurityConstants.IMAGE_CODE_PREFIX + vcodeId, capText, 300);
        } catch (Exception e) {
            logger.error("验证码存取异常：",e);
        }
        BufferedImage image = producer.createImage(capText);
        ServletOutputStream out = rsp.getOutputStream();
        ImageIO.write(image, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
}