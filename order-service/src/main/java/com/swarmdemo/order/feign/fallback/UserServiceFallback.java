package com.swarmdemo.order.feign.fallback;

import com.swarmdemo.order.dto.UserDto;
import com.swarmdemo.order.feign.UserServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 用户服务降级处理
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Component
public class UserServiceFallback implements UserServiceClient {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceFallback.class);

    @Override
    public UserDto getUserById(Long id) {
        logger.warn("用户服务调用失败，执行降级处理，用户ID: {}", id);
        
        UserDto fallbackUser = new UserDto();
        fallbackUser.setId(id);
        fallbackUser.setName("未知用户");
        fallbackUser.setEmail("unknown@example.com");
        fallbackUser.setAge(0);
        
        return fallbackUser;
    }
}