package com.bochao.common.entity;


import lombok.Data;

import java.io.Serializable;

/**
 * 角色表
 * @author
 */
@Data
public class AuthRole implements Serializable {

    private String roleName;//角色名称

    private Integer id;//编码

    private Integer roleCode;//角色编码


}
