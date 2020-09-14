package com.bochao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@ServletComponentScan(basePackages = "com.bochao.*")
@MapperScan({ "com.bochao.*.mapper", "com.bochao.*.*.mapper", "com.bochao.*.*.*.mapper"})
public class App   {
	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

}
