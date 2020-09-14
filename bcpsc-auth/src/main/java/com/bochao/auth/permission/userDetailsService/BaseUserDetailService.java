package com.bochao.auth.permission.userDetailsService;

import com.bochao.auth.feign.UserService;
import com.bochao.auth.permission.token.BaseUserDetail;
import com.bochao.common.entity.AuthUser;
import com.bochao.common.entity.Permission;
import com.bochao.common.exception.AuthException;
import com.bochao.common.pojo.Constant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


/**
 * @Author:
 * @Date 17:01 2020/7/27
 */
public abstract class BaseUserDetailService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 用户业务接口
     */
    @Resource
    protected UserService userService;


    @Autowired
    private RedisTemplate<String, String> permissionRedisTemplate;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if(attributes==null){
            throw new AuthException("获取不到当先请求");
        }
        HttpServletRequest request = attributes.getRequest();
        String clientId = request.getParameter("client_id");
        AuthUser authUserInfo = getUser(username,clientId);

        List<GrantedAuthority> authorities = new ArrayList<>() ;
        //查询角色列表
        String[] roleCodes = authUserInfo.getRoleCodes().split(",");
        for(String roleCode : roleCodes){
            //只存储角色，所以不需要做区别判断
            authorities.add(new SimpleGrantedAuthority(roleCode.toString()));
            List<Permission> permissions = userService.listByRole(Integer.parseInt(roleCode)).getData();
            //存储权限到redis集合,保持颗粒度细化，当然也可以根据用户存储
            storePermission(permissions,roleCode);
        }
        // 返回带有用户权限信息的User
        org.springframework.security.core.userdetails.User user =
                new org.springframework.security.core.userdetails.User(
                        StringUtils.isBlank(authUserInfo.getTelephone())? authUserInfo.getEmail(): authUserInfo.getTelephone(),
                        authUserInfo.getPassword(),
                        isActive(authUserInfo.getStates()),
                        true,
                        true,
                        true, authorities);

        return new BaseUserDetail(authUserInfo, user);
    }

    /**
     * 存储权限
     * @param permissions
     */
    private void storePermission( List<Permission> permissions,String roleCode){
        String redisKey = Constant.PERMISSIONS +roleCode;
        // 清除 Redis 中用户的角色
        permissionRedisTemplate.delete(redisKey);
        permissions.forEach(permission -> {
            permissionRedisTemplate.opsForList().rightPush(redisKey,permission.getRequestUrl());
        });
    }
    /**
     * 获取用户
     * @param userName
     * @return
     */
    protected abstract AuthUser getUser(String userName, String clientId) ;

    /**
     * 是否有效的
     * @param active
     * @return
     */
    private boolean isActive(Integer active){
        if(1==active){
            return true;
        }
        return false;
    }

}