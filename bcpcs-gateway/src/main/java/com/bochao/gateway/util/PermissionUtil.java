package com.bochao.gateway.util;

import com.alibaba.fastjson.JSONArray;
import com.bochao.common.pojo.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限工具类
 *
 * @author 陈晓峰
 */
public class PermissionUtil {

    @Autowired
    private RedisTemplate<String, String> permissionRedisTemplate;

    /**
     * 根据角色获取权限列表
     *
     * @param array
     * @return
     */
    public List<String> getResultPermission(JSONArray array) {
        List<String> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            String roleCode = array.getString(i);
            List<String> permissions = getPermissions(roleCode);
            result.addAll(permissions);
        }
        if (result.size() > 0) {
            result = result.stream().distinct().collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 根据角色获取所有的权限
     *
     * @param roleCode
     * @return
     */
    public List<String> getResultPermission(String roleCode) {
        List<String> result = new ArrayList<>();
        List<String> permissions = getPermissions(roleCode);
        result.addAll(permissions);
        if (result.size() > 0) {
            result = result.stream().distinct().collect(Collectors.toList());
        }
        return result;
    }

    /**
     * 获取当前用户的权限集合
     *
     * @param authority
     * @return
     */
    private List<String> getPermissions(String authority) {
        String redisKey = Constant.PERMISSIONS + authority;
        long size = permissionRedisTemplate.opsForList().size(redisKey);
        List<String> permissions = permissionRedisTemplate.opsForList().range(redisKey, 0, size);
        return permissions;
    }


}
