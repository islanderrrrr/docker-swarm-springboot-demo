package com.swarmdemo.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局日志过滤器
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Component
public class LoggingGlobalFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(LoggingGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        
        // 记录请求信息
        logger.info("Gateway Request: {} {} from {}",
                request.getMethod(),
                request.getURI(),
                request.getRemoteAddress());

        // 记录请求头信息
        request.getHeaders().forEach((name, values) -> {
            if (name.toLowerCase().contains("authorization") || 
                name.toLowerCase().contains("cookie")) {
                logger.debug("Request Header: {}=[PROTECTED]", name);
            } else {
                logger.debug("Request Header: {}={}", name, values);
            }
        });

        long startTime = System.currentTimeMillis();

        return chain.filter(exchange).then(
                Mono.fromRunnable(() -> {
                    long endTime = System.currentTimeMillis();
                    logger.info("Gateway Response: {} {} - Status: {} - Duration: {}ms",
                            request.getMethod(),
                            request.getURI(),
                            exchange.getResponse().getStatusCode(),
                            endTime - startTime);
                })
        );
    }

    @Override
    public int getOrder() {
        return -1;
    }
}