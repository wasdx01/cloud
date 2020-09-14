package com.bochao.system.role.controller;

import com.bochao.common.pojo.Result;
import com.bochao.system.role.entity.Role;
import com.bochao.system.role.service.RoleService;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Api(tags = "角色管理")
@RequestMapping("/role")
public class RoleController {

	@Resource
	private RoleService roleService;

	@RequestMapping(value = "/listRole")
	@ResponseBody
	public Result listRole(Role role) {
		try {
			return Result.success(roleService.findForPage(role));
		}catch(Exception e){
			e.printStackTrace();
			return Result.failure("查询失败！");
		}

	}
}
