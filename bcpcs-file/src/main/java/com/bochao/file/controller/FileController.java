package com.bochao.file.controller;

import com.bochao.common.entity.FileInfo;
import com.bochao.common.pojo.Result;
import com.bochao.file.service.FileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 陈晓峰
 * @Description:
 * @Date:Created in 2020/8/6
 */
@RestController
@Api(tags = "文件信息接口")
@RequestMapping(value = "/file")
public class FileController {
    @Autowired
    private FileInfoService fileInfoService;

    @GetMapping("/getFile/{id}")
    @ApiOperation(value = "通过ID获取文件")
    public Result<FileInfo> getFileById(@PathVariable("id") String id){
        return Result.successResponse(fileInfoService.getFileById(id));
    }
}
