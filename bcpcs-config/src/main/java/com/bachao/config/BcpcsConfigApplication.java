package com.bachao.config;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * The class Paas cloud discovery application.
 *
 * @author paascloud.net@gmail.com
 */
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class BcpcsConfigApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(BcpcsConfigApplication.class, args);
	}
}
