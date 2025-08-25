package com.swarmdemo.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * 用户服务主启动类
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@SpringBootApplication
@EnableEurekaClient
@EnableCaching
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
        System.out.println("=================================");
        System.out.println("User Service 启动成功！");
        System.out.println("访问地址: http://localhost:8081");
        System.out.println("=================================");
    }
}