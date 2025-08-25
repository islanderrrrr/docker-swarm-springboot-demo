package com.swarmdemo.user.exception;

/**
 * 重复资源异常
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
    
    public DuplicateResourceException(String message, Throwable cause) {
        super(message, cause);
    }
}