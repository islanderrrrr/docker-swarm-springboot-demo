package com.swarmdemo.order.feign;

import com.swarmdemo.order.dto.UserDto;
import com.swarmdemo.order.feign.fallback.UserServiceFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 用户服务Feign客户端
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@FeignClient(
    name = "user-service",
    fallback = UserServiceFallback.class
)
public interface UserServiceClient {

    /**
     * 根据用户ID获取用户信息
     */
    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);
}