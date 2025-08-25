package com.swarmdemo.order.exception;

/**
 * 资源未找到异常
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}