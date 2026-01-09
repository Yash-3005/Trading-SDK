package com.bajaj.trading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Portfolio Entity - Represents user's stock holdings
 * Shows what stocks the user currently owns
 */
@Entity
@Table(name = "portfolio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Portfolio {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String userId;
    
    @Column(nullable = false)
    private String symbol;
    
    @Column(nullable = false)
    private Integer quantity;  // How many shares owned
    
    @Column(nullable = false)
    private BigDecimal averagePrice;  // Average buying price
    
    @Column(nullable = false)
    private BigDecimal currentValue;  // Current market value
    
    // Calculate current value based on latest price
    public void updateCurrentValue(BigDecimal currentPrice) {
        this.currentValue = currentPrice.multiply(BigDecimal.valueOf(quantity));
    }
    
    // Update holdings after a BUY trade
    public void addShares(Integer qty, BigDecimal buyPrice) {
        // Calculate new average price using weighted average
        // Formula: ((oldQty × oldPrice) + (newQty × newPrice)) / (oldQty + newQty)
        
        BigDecimal oldTotalValue = averagePrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal newTotalValue = buyPrice.multiply(BigDecimal.valueOf(qty));
        
        this.quantity += qty;
        this.averagePrice = oldTotalValue.add(newTotalValue)
                .divide(BigDecimal.valueOf(this.quantity), 2, RoundingMode.HALF_UP);
    }
    
    // Update holdings after a SELL trade
    public void removeShares(Integer qty) {
        this.quantity -= qty;
        // Average price remains same when selling
    }
    
    // Constructor for creating new portfolio entry
    public Portfolio(String userId, String symbol, Integer quantity, BigDecimal averagePrice) {
        this.userId = userId;
        this.symbol = symbol;
        this.quantity = quantity;
        this.averagePrice = averagePrice;
        this.currentValue = averagePrice.multiply(BigDecimal.valueOf(quantity));
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is Portfolio?
 * A: It's the user's "wallet" showing what stocks they own
 *    Example Portfolio:
 *    - RELIANCE: 10 shares, bought at avg ₹2400, now worth ₹25,000
 *    - TCS: 5 shares, bought at avg ₹3500, now worth ₹18,000
 * 
 * Q: What is averagePrice and why is it important?
 * A: It's the average price at which you bought the stock
 *    
 *    Scenario:
 *    - Day 1: Buy 10 shares at ₹100 each
 *    - Day 2: Buy 5 more shares at ₹110 each
 *    
 *    Average Price = ((10 × 100) + (5 × 110)) / 15
 *                  = (1000 + 550) / 15
 *                  = ₹103.33 per share
 *    
 *    This helps calculate profit/loss:
 *    If current price is ₹120, profit per share = 120 - 103.33 = ₹16.67
 * 
 * Q: Why separate quantity and currentValue?
 * A: quantity = how many shares (doesn't change with price)
 *    currentValue = current market worth (changes with price)
 *    
 *    Example: You own 10 shares of TCS
 *    - Bought at ₹3000 (averagePrice)
 *    - Current price ₹3500
 *    - quantity = 10
 *    - currentValue = 10 × 3500 = ₹35,000
 *    - Your profit = currentValue - (quantity × averagePrice)
 *                  = 35,000 - 30,000 = ₹5,000
 * 
 * Q: What happens to portfolio when you sell?
 * A: Quantity reduces, but average price stays same
 *    You don't "forget" what you bought it for!
 */