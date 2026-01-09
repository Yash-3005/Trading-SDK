package com.bajaj.trading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Trade {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tradeId;
    
    @Column(nullable = false)
    private Long orderId;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Order.OrderType tradeType;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private BigDecimal executedPrice;
    
    @Column(nullable = false)
    private BigDecimal totalValue;
    
    @Column(nullable = false)
    private LocalDateTime executedAt;
    
    private String userId;
    
    public Trade(Long orderId, String symbol, Order.OrderType tradeType, 
                 Integer quantity, BigDecimal executedPrice, String userId) {
        this.orderId = orderId;
        this.symbol = symbol;
        this.tradeType = tradeType;
        this.quantity = quantity;
        this.executedPrice = executedPrice;
        this.totalValue = executedPrice.multiply(BigDecimal.valueOf(quantity));
        this.executedAt = LocalDateTime.now();
        this.userId = userId;
    }
}