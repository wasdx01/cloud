package com.bochao.system.user.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bochao.system.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@TableName("s_user")
public class User extends BaseEntity  {

    @Length(min=0, max=20, message="{user.userName.length.error}")
    private String userName;//用户名

    @Length(min=0, max=32, message="{user.password.length.error}")
    private String password;//密码

    @Length(min=0, max=20, message="{user.realName.length.error}")
    private String realName;//真实姓名

    private Integer sex;//性别

    private Integer age;//年龄

    @Length(min=0, max=10, message="{user.jobNumber.length.error}")
    private String jobNumber;//工号

    @Length(min=0, max=32, message="{user.idNumber.length.error}")
    private String idNumber;//身份证号

    @Length(min=0, max=20, message="{user.mobile.length.error}")
    private String mobile;//手机

    @Length(min=0, max=100, message="{user.email.length.error}")
    private String email;//电子邮箱

    private Integer workUnits;//归属单位

    private Integer deptId;//归属部门

    private Integer states;//状态

    @Length(min=0, max=20, message="{user.loginIp.length.error}")
    private String loginIp;//上次登录IP

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate loginDate;//上次登录时间

    private Integer createBy;//创建人

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate createDate;//创建时间

    @Length(min=0, max=1000, message="{user.memo.length.error}")
    private String memo;//备注
    /**
     * 是否删除(0:不是;1:是)
     */
    private Boolean isDelete;


}