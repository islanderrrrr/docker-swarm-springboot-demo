package com.swarmdemo.order.service;

import com.swarmdemo.order.entity.Order;
import com.swarmdemo.order.vo.OrderVo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 订单服务接口
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
public interface OrderService {

    /**
     * 创建订单
     */
    Order createOrder(Order order);

    /**
     * 根据ID查找订单
     */
    Optional<Order> findById(Long id);

    /**
     * 根据ID查找订单详情（包含用户信息）
     */
    Optional<OrderVo> findOrderVoById(Long id);

    /**
     * 获取所有订单
     */
    List<Order> findAllOrders();

    /**
     * 获取所有订单详情（包含用户信息）
     */
    List<OrderVo> findAllOrderVos();

    /**
     * 分页查询订单
     */
    Page<Order> findOrders(Pageable pageable);

    /**
     * 分页查询订单详情
     */
    Page<OrderVo> findOrderVos(Pageable pageable);

    /**
     * 根据用户ID查找订单
     */
    List<Order> findOrdersByUserId(Long userId);

    /**
     * 根据用户ID查找订单详情
     */
    List<OrderVo> findOrderVosByUserId(Long userId);

    /**
     * 分页查询用户订单
     */
    Page<Order> findOrdersByUserId(Long userId, Pageable pageable);

    /**
     * 分页查询用户订单详情
     */
    Page<OrderVo> findOrderVosByUserId(Long userId, Pageable pageable);

    /**
     * 根据状态查找订单
     */
    List<Order> findOrdersByStatus(Order.OrderStatus status);

    /**
     * 分页查询指定状态的订单
     */
    Page<Order> findOrdersByStatus(Order.OrderStatus status, Pageable pageable);

    /**
     * 更新订单
     */
    Order updateOrder(Long id, Order order);

    /**
     * 更新订单状态
     */
    Order updateOrderStatus(Long id, Order.OrderStatus status);

    /**
     * 删除订单
     */
    void deleteOrder(Long id);

    /**
     * 根据商品名称搜索订单
     */
    List<Order> searchOrdersByProductName(String productName);

    /**
     * 根据价格范围查询订单
     */
    List<Order> findOrdersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * 根据时间范围查询订单
     */
    List<Order> findOrdersByDateRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取订单统计信息
     */
    OrderStatistics getOrderStatistics();

    /**
     * 获取用户订单统计信息
     */
    UserOrderStatistics getUserOrderStatistics(Long userId);

    /**
     * 获取热门商品
     */
    List<PopularProduct> getPopularProducts(int limit);

    /**
     * 订单统计信息内部类
     */
    class OrderStatistics {
        private Long totalOrders;
        private Long completedOrders;
        private Long pendingOrders;
        private BigDecimal totalRevenue;
        private BigDecimal completedRevenue;

        public OrderStatistics(Long totalOrders, Long completedOrders, Long pendingOrders, 
                             BigDecimal totalRevenue, BigDecimal completedRevenue) {
            this.totalOrders = totalOrders;
            this.completedOrders = completedOrders;
            this.pendingOrders = pendingOrders;
            this.totalRevenue = totalRevenue;
            this.completedRevenue = completedRevenue;
        }

        // Getter方法
        public Long getTotalOrders() { return totalOrders; }
        public Long getCompletedOrders() { return completedOrders; }
        public Long getPendingOrders() { return pendingOrders; }
        public BigDecimal getTotalRevenue() { return totalRevenue; }
        public BigDecimal getCompletedRevenue() { return completedRevenue; }

        // Setter方法
        public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }
        public void setCompletedOrders(Long completedOrders) { this.completedOrders = completedOrders; }
        public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }
        public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
        public void setCompletedRevenue(BigDecimal completedRevenue) { this.completedRevenue = completedRevenue; }
    }

    /**
     * 用户订单统计信息内部类
     */
    class UserOrderStatistics {
        private Long userId;
        private Long orderCount;
        private BigDecimal totalAmount;

        public UserOrderStatistics(Long userId, Long orderCount, BigDecimal totalAmount) {
            this.userId = userId;
            this.orderCount = orderCount;
            this.totalAmount = totalAmount;
        }

        // Getter方法
        public Long getUserId() { return userId; }
        public Long getOrderCount() { return orderCount; }
        public BigDecimal getTotalAmount() { return totalAmount; }

        // Setter方法
        public void setUserId(Long userId) { this.userId = userId; }
        public void setOrderCount(Long orderCount) { this.orderCount = orderCount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    }

    /**
     * 热门商品内部类
     */
    class PopularProduct {
        private String productName;
        private Long totalQuantity;

        public PopularProduct(String productName, Long totalQuantity) {
            this.productName = productName;
            this.totalQuantity = totalQuantity;
        }

        // Getter方法
        public String getProductName() { return productName; }
        public Long getTotalQuantity() { return totalQuantity; }

        // Setter方法
        public void setProductName(String productName) { this.productName = productName; }
        public void setTotalQuantity(Long totalQuantity) { this.totalQuantity = totalQuantity; }
    }
}