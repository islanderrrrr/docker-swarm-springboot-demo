package com.swarmdemo.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Spring Cloud Config 配置中心
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class ConfigServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
        System.out.println("=================================");
        System.out.println("Config Server 启动成功！");
        System.out.println("访问地址: http://localhost:8888");
        System.out.println("=================================");
    }
}