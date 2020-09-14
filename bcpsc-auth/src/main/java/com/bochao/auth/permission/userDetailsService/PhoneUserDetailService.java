package com.bochao.auth.permission.userDetailsService;

import com.bochao.common.entity.AuthUser;
import com.bochao.common.exception.AuthException;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Date 17:30 2019/11/27
 */
@Service
public class PhoneUserDetailService extends BaseUserDetailService {


    @Override
    protected AuthUser getUser(String telephone, String clientId) {
        AuthUser authUser = userService.getUserByTel(telephone).getData();
        if(authUser ==null){
            throw new AuthException("用户不存在");
        }
        return authUser;
    }
}
