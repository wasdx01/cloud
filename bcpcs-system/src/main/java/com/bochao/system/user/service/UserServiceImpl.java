package com.bochao.system.user.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bochao.common.entity.AuthUser;
import com.bochao.system.user.entity.User;
import com.bochao.system.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *@Author 陈晓峰
 *@Description:
 *@Date:Created
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;


    @Override
    public AuthUser getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    @Override
    public AuthUser getUserByTel(String telephone) {
        return userMapper.getUserByTel(telephone);
    }


}
