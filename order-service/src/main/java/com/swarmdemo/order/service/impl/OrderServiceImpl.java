package com.swarmdemo.order.service.impl;

import com.swarmdemo.order.dto.UserDto;
import com.swarmdemo.order.entity.Order;
import com.swarmdemo.order.exception.ResourceNotFoundException;
import com.swarmdemo.order.exception.BusinessException;
import com.swarmdemo.order.feign.UserServiceClient;
import com.swarmdemo.order.repository.OrderRepository;
import com.swarmdemo.order.service.OrderService;
import com.swarmdemo.order.vo.OrderVo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 订单服务实现类
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserServiceClient userServiceClient;

    @Override
    public Order createOrder(Order order) {
        logger.info("创建订单，用户ID: {}, 商品: {}", order.getUserId(), order.getProductName());
        
        // 验证用户是否存在
        try {
            UserDto user = getUserWithCircuitBreaker(order.getUserId());
            if (user == null || "未知用户".equals(user.getName())) {
                throw new BusinessException("用户不存在，无法创建订单");
            }
        } catch (Exception e) {
            logger.warn("用户服务调用失败，但仍然创建订单: {}", e.getMessage());
        }

        Order savedOrder = orderRepository.save(order);
        logger.info("订单创建成功，ID: {}", savedOrder.getId());
        return savedOrder;
    }

    @CircuitBreaker(name = "user-service", fallbackMethod = "getUserFallback")
    private UserDto getUserWithCircuitBreaker(Long userId) {
        return userServiceClient.getUserById(userId);
    }

    private UserDto getUserFallback(Long userId, Exception ex) {
        logger.warn("用户服务调用失败，使用降级方案，用户ID: {}, 异常: {}", userId, ex.getMessage());
        UserDto fallbackUser = new UserDto();
        fallbackUser.setId(userId);
        fallbackUser.setName("未知用户");
        fallbackUser.setEmail("unknown@example.com");
        fallbackUser.setAge(0);
        return fallbackUser;
    }

    @Override
    @Cacheable(value = "orders", key = "#id")
    public Optional<Order> findById(Long id) {
        logger.info("查找订单，ID: {}", id);
        return orderRepository.findById(id);
    }

    @Override
    public Optional<OrderVo> findOrderVoById(Long id) {
        logger.info("查找订单详情，ID: {}", id);
        Optional<Order> orderOpt = orderRepository.findById(id);
        
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            UserDto user = getUserWithCircuitBreaker(order.getUserId());
            return Optional.of(new OrderVo(order, user));
        }
        
        return Optional.empty();
    }

    @Override
    @Cacheable(value = "ordersList", key = "'all'")
    public List<Order> findAllOrders() {
        logger.info("获取所有订单");
        return orderRepository.findAll();
    }

    @Override
    public List<OrderVo> findAllOrderVos() {
        logger.info("获取所有订单详情");
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(order -> {
                    UserDto user = getUserWithCircuitBreaker(order.getUserId());
                    return new OrderVo(order, user);
                })
                .collect(Collectors.toList());
    }

    @Override
    public Page<Order> findOrders(Pageable pageable) {
        logger.info("分页查询订单，页码: {}, 大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<OrderVo> findOrderVos(Pageable pageable) {
        logger.info("分页查询订单详情，页码: {}, 大小: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orders = orderRepository.findAll(pageable);
        
        List<OrderVo> orderVos = orders.getContent().stream()
                .map(order -> {
                    UserDto user = getUserWithCircuitBreaker(order.getUserId());
                    return new OrderVo(order, user);
                })
                .collect(Collectors.toList());
        
        return new PageImpl<>(orderVos, pageable, orders.getTotalElements());
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        logger.info("查找用户订单，用户ID: {}", userId);
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<OrderVo> findOrderVosByUserId(Long userId) {
        logger.info("查找用户订单详情，用户ID: {}", userId);
        List<Order> orders = orderRepository.findByUserId(userId);
        UserDto user = getUserWithCircuitBreaker(userId);
        
        return orders.stream()
                .map(order -> new OrderVo(order, user))
                .collect(Collectors.toList());
    }

    @Override
    public Page<Order> findOrdersByUserId(Long userId, Pageable pageable) {
        logger.info("分页查询用户订单，用户ID: {}, 页码: {}, 大小: {}", 
                   userId, pageable.getPageNumber(), pageable.getPageSize());
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<OrderVo> findOrderVosByUserId(Long userId, Pageable pageable) {
        logger.info("分页查询用户订单详情，用户ID: {}, 页码: {}, 大小: {}", 
                   userId, pageable.getPageNumber(), pageable.getPageSize());
        Page<Order> orders = orderRepository.findByUserId(userId, pageable);
        UserDto user = getUserWithCircuitBreaker(userId);
        
        List<OrderVo> orderVos = orders.getContent().stream()
                .map(order -> new OrderVo(order, user))
                .collect(Collectors.toList());
        
        return new PageImpl<>(orderVos, pageable, orders.getTotalElements());
    }

    @Override
    public List<Order> findOrdersByStatus(Order.OrderStatus status) {
        logger.info("根据状态查找订单: {}", status);
        return orderRepository.findByStatus(status);
    }

    @Override
    public Page<Order> findOrdersByStatus(Order.OrderStatus status, Pageable pageable) {
        logger.info("分页查询指定状态的订单: {}, 页码: {}, 大小: {}", 
                   status, pageable.getPageNumber(), pageable.getPageSize());
        return orderRepository.findByStatus(status, pageable);
    }

    @Override
    @CacheEvict(value = {"orders", "ordersList"}, key = "#id")
    public Order updateOrder(Long id, Order order) {
        logger.info("更新订单，ID: {}", id);
        
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));

        existingOrder.setProductName(order.getProductName());
        existingOrder.setQuantity(order.getQuantity());
        existingOrder.setPrice(order.getPrice());
        existingOrder.setStatus(order.getStatus());

        Order updatedOrder = orderRepository.save(existingOrder);
        logger.info("订单更新成功，ID: {}", updatedOrder.getId());
        return updatedOrder;
    }

    @Override
    @CacheEvict(value = {"orders", "ordersList"}, key = "#id")
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        logger.info("更新订单状态，ID: {}, 状态: {}", id, status);
        
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("订单不存在，ID: " + id));

        existingOrder.setStatus(status);
        Order updatedOrder = orderRepository.save(existingOrder);
        logger.info("订单状态更新成功，ID: {}, 新状态: {}", updatedOrder.getId(), status);
        return updatedOrder;
    }

    @Override
    @CacheEvict(value = {"orders", "ordersList"}, key = "#id")
    public void deleteOrder(Long id) {
        logger.info("删除订单，ID: {}", id);
        
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("订单不存在，ID: " + id);
        }

        orderRepository.deleteById(id);
        logger.info("订单删除成功，ID: {}", id);
    }

    @Override
    public List<Order> searchOrdersByProductName(String productName) {
        logger.info("根据商品名称搜索订单: {}", productName);
        return orderRepository.findByProductNameContainingIgnoreCase(productName);
    }

    @Override
    public List<Order> findOrdersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        logger.info("根据价格范围查询订单: {} - {}", minPrice, maxPrice);
        return orderRepository.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Order> findOrdersByDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("根据时间范围查询订单: {} - {}", startTime, endTime);
        return orderRepository.findByCreatedAtBetween(startTime, endTime);
    }

    @Override
    @Cacheable(value = "orderStatistics", key = "'stats'")
    public OrderStatistics getOrderStatistics() {
        logger.info("获取订单统计信息");
        
        Long totalOrders = orderRepository.count();
        Long completedOrders = orderRepository.countByStatus(Order.OrderStatus.COMPLETED);
        Long pendingOrders = totalOrders - completedOrders;
        
        BigDecimal totalRevenue = orderRepository.calculateTotalAmountByStatus(null);
        BigDecimal completedRevenue = orderRepository.calculateTotalAmountByStatus(Order.OrderStatus.COMPLETED);
        
        if (totalRevenue == null) totalRevenue = BigDecimal.ZERO;
        if (completedRevenue == null) completedRevenue = BigDecimal.ZERO;

        return new OrderStatistics(totalOrders, completedOrders, pendingOrders, totalRevenue, completedRevenue);
    }

    @Override
    public UserOrderStatistics getUserOrderStatistics(Long userId) {
        logger.info("获取用户订单统计信息，用户ID: {}", userId);
        
        Long orderCount = orderRepository.countByUserId(userId);
        BigDecimal totalAmount = orderRepository.calculateTotalAmountByUserId(userId);
        
        if (totalAmount == null) totalAmount = BigDecimal.ZERO;

        return new UserOrderStatistics(userId, orderCount, totalAmount);
    }

    @Override
    public List<PopularProduct> getPopularProducts(int limit) {
        logger.info("获取热门商品，限制: {}", limit);
        
        Pageable pageable = PageRequest.of(0, limit);
        List<Object[]> results = orderRepository.findPopularProducts(pageable);
        
        return results.stream()
                .map(result -> new PopularProduct((String) result[0], ((Number) result[1]).longValue()))
                .collect(Collectors.toList());
    }
}