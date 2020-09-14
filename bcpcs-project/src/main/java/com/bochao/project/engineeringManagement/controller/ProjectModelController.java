package com.bochao.project.engineeringManagement.controller;


import com.bochao.common.entity.FileInfo;
import com.bochao.common.mq.annotation.EnableRabbitMq;
import com.bochao.common.mq.config.RabbitConfig;
import com.bochao.common.mq.service.ProducerService;
import com.bochao.common.pojo.Result;
import com.bochao.common.util.UUIDUtil;
import com.bochao.project.engineeringManagement.entity.ModelAnalysis;
import com.bochao.project.engineeringManagement.entity.ProjectSub;
import com.bochao.project.feign.FileService;
import com.bochao.project.engineeringManagement.service.ModelAnalysisService;
import com.bochao.project.model.entity.GimMqMessage;
import com.bochao.project.model.service.ModelService;
import com.bochao.project.engineeringManagement.service.ProjectSubService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;


/**
 * @Author 陈晓峰
 * @Description //TODO 工程模型管理
 * @Date 2020/8/6
 * @Param
 * @return
 **/
@RestController
@Api(tags = "工程模型管理")
@RequestMapping("/projectModel")
@EnableRabbitMq
public class ProjectModelController extends CommonController {
    @Resource
    private FileService fileService;
    @Resource
    private ProjectSubService projectSubService;
    @Resource
    private ModelAnalysisService modelAnalysisService;
    @Resource
    private ModelService modelService;
    @Resource
    private ProducerService producerService;
    /**
     * @Author 陈晓峰
     * @Description //TODO 模型文件解析
     * @Date 2020/8/6
     * @Param [projectId, fileInfoId]
     **/
    @ResponseBody
    @RequestMapping("/saveModelFile")
    @ApiOperation(value = "下载文件", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projectId", value = "工程ID", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "file", value = "模型文件", dataType = "file", paramType = "query")})
    public Object saveModelFile(String projectId, @RequestParam("file") MultipartFile file) {
        try {
            String suffix = file.getOriginalFilename().replaceAll(".*\\.(\\w+$)", "$1");
            if (!"gim".equalsIgnoreCase(suffix) && !"udf".equalsIgnoreCase(suffix)) {
                return Result.failure("模型文件类型出错！");
            }
            Result<FileInfo> r = fileService.handleFileUpload(file);
            FileInfo fileInfo;
            if (r.isSuccess()) {
                fileInfo = r.getData();
            } else {
                return Result.failure("上传失败！", "文件上传出错");
            }
            ModelAnalysis model = new ModelAnalysis();
            model.setId(UUIDUtil.getPrimaryKeyUUID());
            model.setProjectId(projectId);
            model.setGimFileId(fileInfo.getId());
            model.setStatus(0);
            model.setUploadDate(new Date());
            // 解析模型
            String filename = fileInfo.getFileName();
            String name = filename + "." + fileInfo.getFileSuffix();
            String path = fileInfo.getStorePath();
            ProjectSub projectSub = projectSubService.getById(projectId);
            modelAnalysisService.delModelByProjectId(projectId);
            producerService.sendMsg(new GimMqMessage(model,path,name,projectSub.getProjectType()), RabbitConfig.UPLOAD_MODEL_FILE_EXCHANGE, RabbitConfig.UPLOAD_MODEL_FILE_ROUTINGKEY);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("上传失败！", e);
        }
        return Result.successMsg("上传成功");
    }


}
