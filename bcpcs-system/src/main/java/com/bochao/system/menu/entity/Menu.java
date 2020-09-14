package com.bochao.system.menu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bochao.system.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@TableName("s_menu")
public class Menu extends BaseEntity {

    private Integer parentId;//父ID

    @Length(min=0, max=255, message="{menu.menuName.length.error}")
    private String menuName;//菜单名称

    @Length(min=0, max=255, message="{menu.menuUrl.length.error}")
    private String menuUrl;//菜单路径

    private Integer sortNum;//排序

    private Integer isShow;//是否显示

    private Integer loginType;//登录方式

    private Integer createBy;//创建人

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate createDate;//创建时间

    @Length(min=0, max=1000, message="{menu.memo.length.error}")
    private String memo;//备注

    /**非表结构字段**/
    @TableField(exist = false)
    private List<Menu> children; // 子菜单


    
}