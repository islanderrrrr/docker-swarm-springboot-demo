package com.swarmdemo.order.controller;

import com.swarmdemo.order.entity.Order;
import com.swarmdemo.order.service.OrderService;
import com.swarmdemo.order.vo.OrderVo;
import io.micrometer.core.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单控制器
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@RestController
@RequestMapping("/orders")
@Timed(value = "order.requests", description = "Order API requests")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order) {
        logger.info("创建订单请求，用户ID: {}, 商品: {}", order.getUserId(), order.getProductName());
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        logger.info("获取订单详情，ID: {}", id);
        return orderService.findById(id)
                .map(order -> ResponseEntity.ok(order))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取订单详情（包含用户信息）
     */
    @GetMapping("/{id}/details")
    public ResponseEntity<OrderVo> getOrderVoById(@PathVariable Long id) {
        logger.info("获取订单详情（含用户信息），ID: {}", id);
        return orderService.findOrderVoById(id)
                .map(orderVo -> ResponseEntity.ok(orderVo))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取所有订单
     */
    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders() {
        logger.info("获取所有订单");
        List<Order> orders = orderService.findAllOrders();
        return ResponseEntity.ok(orders);
    }

    /**
     * 获取所有订单详情（包含用户信息）
     */
    @GetMapping("/details")
    public ResponseEntity<List<OrderVo>> getAllOrderVos() {
        logger.info("获取所有订单详情（含用户信息）");
        List<OrderVo> orderVos = orderService.findAllOrderVos();
        return ResponseEntity.ok(orderVos);
    }

    /**
     * 分页查询订单
     */
    @GetMapping("/page")
    public ResponseEntity<Page<Order>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("分页查询订单，页码: {}, 大小: {}, 排序: {} {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.findOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * 分页查询订单详情
     */
    @GetMapping("/page/details")
    public ResponseEntity<Page<OrderVo>> getOrderVos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("分页查询订单详情，页码: {}, 大小: {}, 排序: {} {}", page, size, sortBy, sortDir);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<OrderVo> orderVos = orderService.findOrderVos(pageable);
        return ResponseEntity.ok(orderVos);
    }

    /**
     * 根据用户ID获取订单
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
        logger.info("获取用户订单，用户ID: {}", userId);
        List<Order> orders = orderService.findOrdersByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据用户ID获取订单详情
     */
    @GetMapping("/user/{userId}/details")
    public ResponseEntity<List<OrderVo>> getOrderVosByUserId(@PathVariable Long userId) {
        logger.info("获取用户订单详情，用户ID: {}", userId);
        List<OrderVo> orderVos = orderService.findOrderVosByUserId(userId);
        return ResponseEntity.ok(orderVos);
    }

    /**
     * 分页查询用户订单
     */
    @GetMapping("/user/{userId}/page")
    public ResponseEntity<Page<Order>> getOrdersByUserId(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("分页查询用户订单，用户ID: {}, 页码: {}, 大小: {}", userId, page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.findOrdersByUserId(userId, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据状态查询订单
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Order>> getOrdersByStatus(@PathVariable Order.OrderStatus status) {
        logger.info("根据状态查询订单: {}", status);
        List<Order> orders = orderService.findOrdersByStatus(status);
        return ResponseEntity.ok(orders);
    }

    /**
     * 分页查询指定状态的订单
     */
    @GetMapping("/status/{status}/page")
    public ResponseEntity<Page<Order>> getOrdersByStatus(
            @PathVariable Order.OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        logger.info("分页查询指定状态的订单: {}, 页码: {}, 大小: {}", status, page, size);
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<Order> orders = orderService.findOrdersByStatus(status, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * 更新订单
     */
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @Valid @RequestBody Order order) {
        logger.info("更新订单，ID: {}", id);
        Order updatedOrder = orderService.updateOrder(id, order);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * 更新订单状态
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam Order.OrderStatus status) {
        logger.info("更新订单状态，ID: {}, 状态: {}", id, status);
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOrder(@PathVariable Long id) {
        logger.info("删除订单，ID: {}", id);
        orderService.deleteOrder(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "订单删除成功");
        response.put("orderId", id);
        
        return ResponseEntity.ok(response);
    }

    /**
     * 搜索订单
     */
    @GetMapping("/search")
    public ResponseEntity<List<Order>> searchOrders(@RequestParam String productName) {
        logger.info("搜索订单，商品名称: {}", productName);
        List<Order> orders = orderService.searchOrdersByProductName(productName);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据价格范围查询订单
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Order>> getOrdersByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        
        logger.info("根据价格范围查询订单: {} - {}", minPrice, maxPrice);
        List<Order> orders = orderService.findOrdersByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(orders);
    }

    /**
     * 根据时间范围查询订单
     */
    @GetMapping("/date-range")
    public ResponseEntity<List<Order>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        logger.info("根据时间范围查询订单: {} - {}", startTime, endTime);
        List<Order> orders = orderService.findOrdersByDateRange(startTime, endTime);
        return ResponseEntity.ok(orders);
    }

    /**
     * 获取订单统计信息
     */
    @GetMapping("/statistics")
    public ResponseEntity<OrderService.OrderStatistics> getOrderStatistics() {
        logger.info("获取订单统计信息");
        OrderService.OrderStatistics statistics = orderService.getOrderStatistics();
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取用户订单统计信息
     */
    @GetMapping("/user/{userId}/statistics")
    public ResponseEntity<OrderService.UserOrderStatistics> getUserOrderStatistics(@PathVariable Long userId) {
        logger.info("获取用户订单统计信息，用户ID: {}", userId);
        OrderService.UserOrderStatistics statistics = orderService.getUserOrderStatistics(userId);
        return ResponseEntity.ok(statistics);
    }

    /**
     * 获取热门商品
     */
    @GetMapping("/popular-products")
    public ResponseEntity<List<OrderService.PopularProduct>> getPopularProducts(
            @RequestParam(defaultValue = "10") int limit) {
        logger.info("获取热门商品，限制: {}", limit);
        List<OrderService.PopularProduct> products = orderService.getPopularProducts(limit);
        return ResponseEntity.ok(products);
    }

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("service", "order-service");
        health.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(health);
    }
}