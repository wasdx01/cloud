package com.bochao.file.controller;

import com.bochao.common.entity.FileInfo;
import com.bochao.common.mq.annotation.EnableRabbitMq;
import com.bochao.common.mq.config.RabbitConfig;
import com.bochao.common.mq.entity.ModelFileMqMessage;
import com.bochao.common.mq.service.ProducerService;
import com.bochao.common.pojo.Result;
import com.bochao.file.service.FileInfoService;
import com.bochao.file.util.FileWrite;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * @Author 陈晓峰
 * @Description //TODO 上传文件
 * @Date 2020/8/4
 */
@RestController
@Api(tags = "文件上传")
@RequestMapping
@EnableRabbitMq
public class UploadFileController {
    @Value("${file.path}")
    private String filePath;
    @Autowired
    private FileInfoService fileInfoService;
    @Resource
    private ProducerService producerService;

    /**
     * @return java.lang.Object
     * @Author 陈晓峰
     * @Description //TODO  上传文件
     * @Date 2020/8/4
     * @Param [file]
     **/
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@RequestParam("upfile") MultipartFile[] file) {
        try {
            FileInfo fileInfo = null;
            long path = Instant.now().getEpochSecond();
            for (MultipartFile multipartFile : file) {
                fileInfo = new FileWrite(fileInfoService).write(multipartFile, filePath + path);
                if (fileInfo.getStorePath() != null) {
                    return Result.successResponse(fileInfo);
                } else {
                    return Result.failResponse();
                }
            }
            return Result.successResponse(fileInfo);
        } catch (Exception e) {
            return (e.getMessage());
        }
    }

    /**
     * @return java.lang.Object
     * @Author 陈晓峰
     * @Description //TODO  上传模型文件
     * @Date 2020/8/4
     * @Param [file]
     **/
    @RequestMapping(value = "/uploadModelFile", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@RequestParam("upfile") MultipartFile file, String projectId) {
        try {
            String suffix = file.getOriginalFilename().replaceAll(".*\\.(\\w+$)", "$1");
            if (!"gim".equalsIgnoreCase(suffix) && !"udf".equalsIgnoreCase(suffix)) {
                return Result.failure("模型文件类型出错！");
            }
            long path = Instant.now().getEpochSecond();
            FileInfo fileInfo = new FileWrite(fileInfoService).write(file, filePath + path);
            if (!StringUtils.isEmpty(fileInfo.getStorePath())) {
                producerService.sendMsg(new ModelFileMqMessage(fileInfo, projectId), RabbitConfig.UPLOAD_MODEL_FILE_EXCHANGE, RabbitConfig.UPLOAD_MODEL_FILE_ROUTINGKEY);
                return Result.successMsg("上传成功");
            } else {
                return Result.failure("上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("上传失败");
        }
    }

    /**
     * @return java.lang.Object
     * @Author 陈晓峰
     * @Description //TODO  上传文件  feign接口
     * @Date 2020/8/4
     * @Param [file]
     **/
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(@RequestPart("file") MultipartFile file) {
        try {
            long path = Instant.now().getEpochSecond();
            FileInfo fileInfo = new FileWrite(fileInfoService).write(file, filePath + path);
            if (fileInfo.getStorePath() != null) {
                return Result.successResponse(fileInfo);
            } else {
                return Result.failResponse();
            }
        } catch (Exception e) {
            return Result.failure(e.getMessage());
        }
    }

}
