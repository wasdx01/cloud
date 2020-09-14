package com.bochao.system.role.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bochao.system.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@TableName("s_role")
@Data
public class Role extends BaseEntity {

    private Integer roleCode;//角色编码

    @Length(min = 0, max = 100, message = "{role.roleName.length.error}")
    private String roleName;//角色名称

    private Integer dataScope;//数据范围

    private Integer createBy;//创建人

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate createDate;//创建时间

    @Length(min = 0, max = 1000, message = "{role.memo.length.error}")
    private String memo;//备注

    private static final long serialVersionUID = 1L;
    // 数据范围（1：所有数据；2：所属业主单位数据；3：所在单位数据）
    public static final int DATA_SCOPE_ALL = 1;
    public static final int DATA_SCOPE_COMPANY_AND_CHILD = 2;
    public static final int DATA_SCOPE_COMPANY = 3;


}