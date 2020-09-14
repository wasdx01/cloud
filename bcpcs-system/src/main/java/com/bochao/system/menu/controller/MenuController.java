package com.bochao.system.menu.controller;

import com.bochao.common.entity.Permission;
import com.bochao.common.pojo.Result;
import com.bochao.system.menu.service.MenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
@Api(tags = "权限菜单管理")
@RequestMapping("/menu")
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 用户名
     *
     * @param roleCode
     * @return
     */
    @GetMapping("/list/{roleCode}")
    @ApiOperation(value = "通过用户名获取用户")
    public Result<List<Permission>> listByRole(@PathVariable("roleCode") Integer roleCode) {
        return Result.successResponse(menuService.listByRole(roleCode));
    }
}
