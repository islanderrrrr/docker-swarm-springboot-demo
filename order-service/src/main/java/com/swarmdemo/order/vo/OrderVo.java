package com.swarmdemo.order.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swarmdemo.order.dto.UserDto;
import com.swarmdemo.order.entity.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单视图对象
 * 
 * @author islanderrrrr
 * @since 2025-08-25
 */
public class OrderVo {

    private Long id;
    private Long userId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalAmount;
    private Order.OrderStatus status;
    private UserDto user;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    // 构造方法
    public OrderVo() {}

    public OrderVo(Order order) {
        this.id = order.getId();
        this.userId = order.getUserId();
        this.productName = order.getProductName();
        this.quantity = order.getQuantity();
        this.price = order.getPrice();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }

    public OrderVo(Order order, UserDto user) {
        this(order);
        this.user = user;
    }

    // Getter和Setter方法
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "OrderVo{" +
                "id=" + id +
                ", userId=" + userId +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalAmount=" + totalAmount +
                ", status=" + status +
                ", user=" + user +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}