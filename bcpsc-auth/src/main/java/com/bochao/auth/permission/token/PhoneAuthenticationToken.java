package com.bochao.auth.permission.token;

import com.bochao.auth.permission.token.MyAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 电话token
 * @Author:
 * @Date 16:29 2019/11/27
 */
public class PhoneAuthenticationToken extends MyAuthenticationToken {

    public PhoneAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    public PhoneAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
