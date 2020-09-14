package com.bochao.system.user.controller;

import com.bochao.common.entity.AuthUser;
import com.bochao.common.pojo.Result;
import com.bochao.system.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = "用户管理")
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户名
     * @param userName
     * @return
     */
    @GetMapping("/userName/{userName}")
    @ApiOperation(value = "通过用户名获取用户")
    public Result<AuthUser> getUserByName(@PathVariable("userName") String userName){
        return Result.successResponse(userService.getUserByName(userName));
    }

    /**
     * 电话
     * @param telephone
     * @return
     */
    @GetMapping("/tel/{telephone}")
    @ApiOperation(value = "通过电话号码获取用户")
    public Result<AuthUser> getUserByTel(@PathVariable("telephone") String telephone){
        return Result.successResponse(userService.getUserByTel(telephone));
    }


}
