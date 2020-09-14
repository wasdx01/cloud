package com.bochao.system.role.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bochao.system.role.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface RoleMapper extends BaseMapper<Role> {


}