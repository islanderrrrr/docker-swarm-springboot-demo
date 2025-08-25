package com.swarmdemo.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * Gateway配置类
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 用户服务路由
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.stripPrefix(1)
                                .circuitBreaker(config -> config.setName("user-service-cb")
                                        .setFallbackUri("forward:/fallback/user")))
                        .uri("lb://user-service"))
                
                // 订单服务路由
                .route("order-service", r -> r.path("/api/orders/**")
                        .filters(f -> f.stripPrefix(1)
                                .circuitBreaker(config -> config.setName("order-service-cb")
                                        .setFallbackUri("forward:/fallback/order")))
                        .uri("lb://order-service"))
                
                // Eureka控制台路由
                .route("eureka-web", r -> r.path("/eureka/**")
                        .uri("lb://eureka-server"))
                
                .build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsWebFilter(source);
    }
}