package com.wimp.dreamer.security.auth.dto;

/**
 * 登录用户dto
 * @author zhuyu
 */
public class LoginAuthDto {

    private String id;

    private String username;


    public LoginAuthDto() {
    }

    public LoginAuthDto(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
