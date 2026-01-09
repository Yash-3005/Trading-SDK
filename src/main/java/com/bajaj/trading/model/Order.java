package com.bajaj.trading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStyle orderStyle;
    
    @Column(nullable = false)
    private Integer quantity;
    
    private BigDecimal price;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime executedAt;
    
    private String userId;
    
    public enum OrderType {
        BUY, SELL
    }
    
    public enum OrderStyle {
        MARKET, LIMIT
    }
    
    public enum OrderStatus {
        NEW, PLACED, EXECUTED, CANCELLED
    }
}