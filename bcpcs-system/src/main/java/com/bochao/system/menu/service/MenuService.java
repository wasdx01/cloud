package com.bochao.system.menu.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.bochao.common.entity.Permission;
import com.bochao.system.menu.entity.Menu;

import java.util.List;


public interface MenuService extends IService<Menu> {

    List<Permission> listByRole(Integer roleCode);

}
