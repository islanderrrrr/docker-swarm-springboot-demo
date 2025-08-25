package com.swarmdemo.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 订单服务主启动类
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableCaching
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
        System.out.println("=================================");
        System.out.println("Order Service 启动成功！");
        System.out.println("访问地址: http://localhost:8082");
        System.out.println("=================================");
    }
}