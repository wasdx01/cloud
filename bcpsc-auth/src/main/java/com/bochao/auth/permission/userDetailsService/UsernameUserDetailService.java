package com.bochao.auth.permission.userDetailsService;



import com.bochao.common.entity.AuthUser;
import com.bochao.common.exception.AuthException;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Date 17:35 2019/11/27
 */
@Service
public class UsernameUserDetailService extends BaseUserDetailService {



    @Override
    protected AuthUser getUser(String userName, String clientId) {
        AuthUser authUser = userService.getUserByName(userName).getData();
        if(authUser ==null){
            throw new AuthException("用户不存在");
        }
        return authUser;
    }
}
