package com.bochao.project.feign;



import com.bochao.common.entity.FileInfo;
import com.bochao.common.pojo.Result;
import com.bochao.project.engineeringManagement.entity.UploadFallback;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import feign.codec.Encoder;



/**
 * 上传文件接口
 * @author 陈晓峰
 */
@FeignClient(value = "bcpcs-file", configuration = FileService.MultipartSupportConfig.class,fallback = UploadFallback.class)
public interface FileService {
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    Result<FileInfo> handleFileUpload(@RequestPart(value = "file") MultipartFile file);

    @Configuration
    class MultipartSupportConfig {
        @Bean
        public Encoder feignFormEncoder() {
            return new SpringFormEncoder();
        }
    }

}
