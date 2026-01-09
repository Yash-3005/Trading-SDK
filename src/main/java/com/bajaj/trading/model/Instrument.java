package com.bajaj.trading.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Instrument Entity - Represents a tradable financial instrument (stock)
 * This will be stored as a table in the database
 */
@Entity
@Table(name = "instruments")
@Data  // Lombok: Auto-generates getters, setters, toString, equals, hashCode
@NoArgsConstructor  // Lombok: Generates constructor with no parameters
@AllArgsConstructor // Lombok: Generates constructor with all parameters
public class Instrument {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String symbol;  // e.g., "RELIANCE", "TCS"
    
    @Column(nullable = false)
    private String exchange;  // e.g., "NSE", "BSE"
    
    @Column(nullable = false)
    private String instrumentType;  // e.g., "EQUITY", "DERIVATIVE"
    
    @Column(nullable = false)
    private BigDecimal lastTradedPrice;  // Current price
    
    // Constructor without ID (for creating new instruments)
    public Instrument(String symbol, String exchange, String instrumentType, BigDecimal lastTradedPrice) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.instrumentType = instrumentType;
        this.lastTradedPrice = lastTradedPrice;
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is @Entity?
 * A: It tells Spring Boot "this class represents a database table"
 * 
 * Q: What is @Id and @GeneratedValue?
 * A: @Id marks the primary key field
 *    @GeneratedValue means the database will auto-generate IDs (1, 2, 3...)
 * 
 * Q: Why BigDecimal for price instead of double?
 * A: Financial applications need EXACT precision. 
 *    double has rounding errors: 0.1 + 0.2 = 0.30000000000000004
 *    BigDecimal gives exact values, crucial for money!
 * 
 * Q: What does Lombok @Data do?
 * A: It automatically creates:
 *    - getters: getSymbol(), getExchange()
 *    - setters: setSymbol(), setExchange()
 *    - toString(): for printing object
 *    - equals() and hashCode(): for comparing objects
 *    This saves writing ~50 lines of boilerplate code!
 */