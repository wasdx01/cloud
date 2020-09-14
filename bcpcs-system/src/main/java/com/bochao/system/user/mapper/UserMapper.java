package com.bochao.system.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bochao.common.entity.AuthUser;
import com.bochao.system.user.entity.User;
import org.apache.ibatis.annotations.*;



/**
 * @Author 陈晓峰
 * @Description:
 * @Date:
 */

@Mapper
public interface UserMapper extends BaseMapper<User> {

    AuthUser getUserByName(String userName);

    AuthUser getUserByTel(String telephone);
}
