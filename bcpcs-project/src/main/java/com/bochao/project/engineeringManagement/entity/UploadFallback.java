package com.bochao.project.engineeringManagement.entity;

import com.bochao.common.entity.FileInfo;
import com.bochao.common.pojo.Result;
import com.bochao.project.feign.FileService;
import feign.hystrix.FallbackFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 陈晓峰
 * @Description:
 * @Date:Created in 2020/8/17
 */
public class UploadFallback implements FallbackFactory<FileService> {

    @Override
    public FileService create(Throwable cause) {
        return new FileService() {
            @Override
            public Result<FileInfo> handleFileUpload(MultipartFile file) {
                return Result.failure("上传文件超时");
            }

        };
    }

}
