package com.bochao.system.menu.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bochao.common.entity.Permission;
import com.bochao.system.menu.entity.Menu;
import com.bochao.system.menu.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *@Author 陈晓峰
 *@Description:
 *@Date:Created
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper,Menu> implements MenuService {
    @Resource
    private MenuMapper menuMapper;


    @Override
    public List<Permission> listByRole(Integer roleCode) {
        return menuMapper.listByRole(roleCode);
    }
}
