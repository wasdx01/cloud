package com.bochao.file.controller;


import com.bochao.common.entity.FileInfo;
import com.bochao.file.service.FileInfoService;
import com.bochao.file.util.FileRead;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * @Author 陈晓峰
 * @Date  2020/8/4
 **/
@RestController
@Api(tags = "文件下载")
@RequestMapping
@ConditionalOnProperty(name = "file.down",havingValue = "open")
public class DownFileController {

    @Autowired
    private FileInfoService fileInfoService;

    /**
     * @Author 陈晓峰
     * @Description //文件下载
     * @Date  2020/8/4
     * @Param [request, response, fileId]
     **/
    @ResponseBody
    @ApiOperation(value = "下载文件")
    @ApiImplicitParam(name = "fileId",value = "文件ID",dataType = "String")
    @RequestMapping(value = "/downLoad/{fileId}", method = RequestMethod.GET)
    public void downLoad(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {
        FileInfo fileInfo = fileInfoService.getFileById(fileId);
        if(fileInfo==null){
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write("failed");
            return;
        }
        File file = new File(fileInfo.getStorePath());
        new FileRead(request,response).read(file,fileInfo.getFileName());

    }

    /**
     * 文件下载
     * @param request
     * @param response
     * @param fileId 数据唯一标识
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/download")
    public void downLoadFile(HttpServletRequest request, HttpServletResponse response, String fileId) throws Exception {
        downLoad(request, response, fileId);
    }

}
