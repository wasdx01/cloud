package com.bochao.common.entity;



import lombok.Data;

import java.io.Serializable;

/**
 * 用户实体
 */
@Data
public class AuthUser implements Serializable {

    private Integer id;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 邮箱，用户企业人员进行登录
     */
    private String email;
    /**
     * 电话号码，用户客户登录
     */
    private String telephone;
    /**
     * 密码
     */
    private String password;
    /**
     * 头像
     */
    private String headerUrl;
    /**
     * 登录状态
     */
    private Integer states;//状态

    private String roleCodes;
}
