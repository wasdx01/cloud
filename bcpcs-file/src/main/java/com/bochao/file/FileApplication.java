package com.bochao.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 启动类
 * @author 陈晓峰
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan(basePackages = "com.bochao.*")
@MapperScan({ "com.bochao.*.mapper", "com.bochao.*.*.mapper", "com.bochao.*.*.*.mapper"})
public class FileApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }

//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(FileApplication.class);
//    }
}
