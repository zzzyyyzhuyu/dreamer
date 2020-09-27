package com.wimp.dreamer.security.auth.biz;


import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wimp.dreamer.base.exception.auth.ClientTokenException;
import com.wimp.dreamer.base.utils.RedisUtil;
import com.wimp.dreamer.base.web.biz.BaseBiz;
import com.wimp.dreamer.security.auth.authorization.properties.OAuth2Properties;
import com.wimp.dreamer.security.auth.domain.SysUserAuthentication;
import com.wimp.dreamer.security.auth.domain.User;
import com.wimp.dreamer.security.auth.dto.LoginAuthDto;
import com.wimp.dreamer.security.auth.exception.UserDisabledException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.common.Mapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * UserBiz
 * @author zhuyu
 */
@Service
@Slf4j
public class UserBiz extends BaseBiz<Mapper<User>, User> implements UserDetailsService {

    @Value("${dreamer.oauth2.oauth2.accessTokenValidateSeconds:7200}")
    private long accessTokenValidateSeconds;

    @Resource
    private OAuth2Properties oAuth2Properties;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = mapper.selectOne(new User(username));
        if (user == null) {
            log.error("[{}] 用户名不存在", username);
            throw new BadCredentialsException("用户名不存在");
        }
        if (user.getIsDeleted() != null && user.getIsDeleted() == 1) {
            log.error("[{}] 账号已经被停用", username);
            throw new UserDisabledException("账号已被停用!");
        }
        return new SysUserAuthentication(user.getId(), user.getLoginName(), user.getPassword());
    }

    /**
     * 刷新token
     *
     * @param accessToken  token
     * @param refreshToken 刷新token
     * @param request      请求
     * @return 刷新后的token
     * @throws HttpProcessException http异常
     */
    public Map<String,Object> refreshToken(String accessToken, String refreshToken, HttpServletRequest request) throws HttpProcessException, JsonProcessingException {
        String token;
        Map<String, Object> map = new HashMap<>(2);
        map.put("grant_type", "refresh_token");
        map.put("refresh_token", refreshToken);

        //插件式配置请求参数（网址、请求参数、编码、client）
        Header[] headers = HttpHeader.custom().contentType(HttpHeader.Headers.APP_FORM_URLENCODED).
                authorization(request.getHeader(HttpHeaders.AUTHORIZATION)).build();
        HttpConfig config = HttpConfig.custom().headers(headers).url(oAuth2Properties.getTokenRefreshUrl()).map(map);
        token = HttpClientUtil.post(config);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,Object>tokenMap = objectMapper.readValue(token, Map.class);
        //刷新redis中的用户信息
        LoginAuthDto loginUser = (LoginAuthDto) redisUtil.get(RedisUtil.ACCESS_TOKEN_KEY+accessToken);
        if (loginUser == null) {
            throw new ClientTokenException("token失效");
        }
        redisUtil.set(RedisUtil.ACCESS_TOKEN_KEY + refreshToken,
                loginUser, accessTokenValidateSeconds);
        return tokenMap;
    }

    /**
     * 登录信息处理，存入redis
     *
     * @param token token
     * @param principal principal（登录名）
     */
    public void handlerLoginData(OAuth2AccessToken token, SysUserAuthentication principal) {
        LoginAuthDto authDto = new LoginAuthDto(principal.getId(), principal.getUsername());
        redisUtil.set(RedisUtil.ACCESS_TOKEN_KEY + token.getValue(),
                authDto, accessTokenValidateSeconds);
    }
}
