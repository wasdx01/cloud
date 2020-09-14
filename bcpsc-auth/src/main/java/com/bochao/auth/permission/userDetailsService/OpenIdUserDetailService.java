package com.bochao.auth.permission.userDetailsService;


import com.bochao.common.entity.AuthUser;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Date 2:57 2019/11/28
 */
@Service
public class OpenIdUserDetailService extends BaseUserDetailService {


    @Override
    protected AuthUser getUser(String openId, String clientId) {

        return null;
    }
}