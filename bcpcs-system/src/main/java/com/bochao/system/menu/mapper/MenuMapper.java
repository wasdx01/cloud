package com.bochao.system.menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bochao.common.entity.Permission;
import com.bochao.system.menu.entity.Menu;


import java.util.List;


public interface MenuMapper extends BaseMapper<Menu> {

    List<Permission> listByRole(Integer roleCode);

}