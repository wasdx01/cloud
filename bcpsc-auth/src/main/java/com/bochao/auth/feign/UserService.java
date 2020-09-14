package com.bochao.auth.feign;


import com.bochao.common.entity.Permission;
import com.bochao.common.entity.AuthUser;
import com.bochao.common.pojo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


/**
 * 用户权限接口
 * @author 陈晓峰
 */
@FeignClient(name = "bcpcs-system")
public interface UserService {
    /**
     * 通过用户名，或者用户名获取用户
     * @param userName
     * @return
     */
    @GetMapping("/user/userName/{userName}")
    Result<AuthUser> getUserByName(@PathVariable("userName") String userName);

    /**
     * 通过用户名，或者email获取用户名
     * @param telephone
     * @return
     */
    @GetMapping("/user/tel/{telephone}")
    Result<AuthUser> getUserByTel(@PathVariable("telephone") String telephone);


    /**
     * 通过用户查询权限
     * @param roleCode
     * @return
     */
    @GetMapping("/menu/list/{roleCode}")
    Result<List<Permission>> listByRole(@PathVariable("roleCode") Integer roleCode);
}
