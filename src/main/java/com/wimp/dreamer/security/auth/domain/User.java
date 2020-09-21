package com.wimp.dreamer.security.auth.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;


/**
 * 用户信息表
 * 
 * @author wanwei
 * @email service@wanweitech.cn
 * @date 2018-09-06 14:40:51
 */
@Table(name = "tb_user")
@Getter
@Setter
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Column(name = "LOGIN_NAME")
    private String loginName;
	

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "ENABLE_SHOW")
    private Integer enableShow;

    @Column(name = "ROLE_ID")
    private String roleId;
	

    @Column(name = "CREATE_TIME")
    private Date createTime;
	

    @Column(name = "TELEPHONE")
    private String telephone;
	

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "IS_DELETED")
    private Integer isDeleted;

    public User() {
    }

    public User(String loginName) {
        this.loginName = loginName;
    }


}
