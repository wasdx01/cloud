package com.bochao.common.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 权限表
 * @author
 */
@Data
public class Permission  implements Serializable {


    private String authName;

    private String requestUrl;

    private Integer loginType;//权限类型 登录方式

}
