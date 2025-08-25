package com.swarmdemo.order.exception;

/**
 * 业务异常
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
public class BusinessException extends RuntimeException {
    
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}