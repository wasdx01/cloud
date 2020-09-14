package com.bochao.system.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bochao.common.entity.AuthUser;
import com.bochao.system.user.entity.User;


/**
 *@Author 陈晓峰
 *@Description:
 *@Date:
 */
public interface UserService extends IService<User> {

    AuthUser getUserByName(String name);

    AuthUser getUserByTel(String telephone);


}
