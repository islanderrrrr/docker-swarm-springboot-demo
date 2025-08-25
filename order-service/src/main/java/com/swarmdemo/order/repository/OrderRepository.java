package com.swarmdemo.order.repository;

import com.swarmdemo.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单数据访问接口
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据用户ID查找订单
     */
    List<Order> findByUserId(Long userId);

    /**
     * 分页查询用户订单
     */
    Page<Order> findByUserId(Long userId, Pageable pageable);

    /**
     * 根据状态查找订单
     */
    List<Order> findByStatus(Order.OrderStatus status);

    /**
     * 分页查询指定状态的订单
     */
    Page<Order> findByStatus(Order.OrderStatus status, Pageable pageable);

    /**
     * 根据用户ID和状态查找订单
     */
    List<Order> findByUserIdAndStatus(Long userId, Order.OrderStatus status);

    /**
     * 根据商品名称模糊查询订单
     */
    List<Order> findByProductNameContainingIgnoreCase(String productName);

    /**
     * 根据价格范围查询订单
     */
    List<Order> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * 根据创建时间范围查询订单
     */
    List<Order> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 统计用户订单数量
     */
    Long countByUserId(Long userId);

    /**
     * 统计指定状态的订单数量
     */
    Long countByStatus(Order.OrderStatus status);

    /**
     * 自定义查询：计算用户订单总金额
     */
    @Query("SELECT SUM(o.price * o.quantity) FROM Order o WHERE o.userId = :userId")
    BigDecimal calculateTotalAmountByUserId(@Param("userId") Long userId);

    /**
     * 自定义查询：计算指定状态订单的总金额
     */
    @Query("SELECT SUM(o.price * o.quantity) FROM Order o WHERE o.status = :status")
    BigDecimal calculateTotalAmountByStatus(@Param("status") Order.OrderStatus status);

    /**
     * 自定义查询：获取用户最近的订单
     */
    @Query("SELECT o FROM Order o WHERE o.userId = :userId ORDER BY o.createdAt DESC")
    List<Order> findRecentOrdersByUserId(@Param("userId") Long userId, Pageable pageable);

    /**
     * 自定义查询：获取热门商品
     */
    @Query("SELECT o.productName, SUM(o.quantity) as totalQuantity FROM Order o " +
           "GROUP BY o.productName ORDER BY totalQuantity DESC")
    List<Object[]> findPopularProducts(Pageable pageable);
}