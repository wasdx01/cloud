package com.bochao.system.role.service;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bochao.common.pojo.Constant;
import com.bochao.system.role.entity.Role;
import com.bochao.system.role.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *@Author 陈晓峰
 *@Description:
 *@Date:Created
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Resource
    private RoleMapper roleMapper;


    @Override
    public IPage<Role> findForPage(Role role) {
        int pageNum = 1, pageSize = Constant.DEF_PAGE_SIZE;
        if(role!=null){
            pageNum = role.getPage();
            pageSize = role.getRows();
        }
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        String name = role.getRoleName();
        //讲师级别
        Integer code = role.getRoleCode();
        //开始时间
        String memo = role.getMemo();
        //多条件组合查询
        //判断条件值是否为空,如果不为空拼接条件
        if (!StringUtils.isEmpty(name)){
            //构建条件 模糊查询
            wrapper.like("role_name",name);
        }
        if (code!=null){
            //等于
            wrapper.eq("role_code",code);
        }
        if (!StringUtils.isEmpty(memo)){
            wrapper.like("memo",memo);
        }
        IPage<Role> page = new Page<>(pageNum, pageSize);
        return roleMapper.selectPage( page, wrapper);
    }

}
