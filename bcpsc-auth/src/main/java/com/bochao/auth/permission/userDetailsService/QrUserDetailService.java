package com.bochao.auth.permission.userDetailsService;

import com.bochao.common.entity.AuthUser;
import org.springframework.stereotype.Service;

/**
 * @Author:
 * @Date 17:33 2019/11/27
 */
@Service
public class QrUserDetailService extends BaseUserDetailService {

    @Override
    protected AuthUser getUser(String userName, String clientId) {
        return null;
    }
}
