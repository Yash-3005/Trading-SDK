package com.bajaj.trading.service;

import com.bajaj.trading.model.Order;
import com.bajaj.trading.model.Portfolio;
import com.bajaj.trading.model.Trade;
import com.bajaj.trading.repository.InstrumentRepository;
import com.bajaj.trading.repository.PortfolioRepository;
import com.bajaj.trading.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * ==================== TRADE SERVICE ====================
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {
    
    private final TradeRepository tradeRepository;
    private static final String DEFAULT_USER_ID = "user123";
    
    /**
     * Create a trade when an order is executed
     */
    public Trade createTrade(Order order, BigDecimal executionPrice) {
        log.info("Creating trade for order: {}", order.getOrderId());
        
        Trade trade = new Trade(
            order.getOrderId(),
            order.getSymbol(),
            order.getOrderType(),
            order.getQuantity(),
            executionPrice,
            order.getUserId()
        );
        
        return tradeRepository.save(trade);
    }
    
    /**
     * Get all trades for the user
     */
    public List<Trade> getAllTrades() {
        return tradeRepository.findByUserId(DEFAULT_USER_ID);
    }
    
    /**
     * Get trade by order ID
     */
    public Trade getTradeByOrderId(Long orderId) {
        return tradeRepository.findByOrderId(orderId).orElse(null);
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * ========== TRADE SERVICE ==========
 * 
 * Q: What is the purpose of TradeService?
 * A: Manages executed trades (completed transactions)
 *    - Creates trade record when order executes
 *    - Retrieves trade history
 *    - Links trades back to original orders
 * 
 * Q: Why separate Trade from Order?
 * A: Order = Intent (I want to buy)
 *    Trade = Execution (I actually bought)
 *    
 *    Separation allows:
 *    - Track order lifecycle (NEW → PLACED → EXECUTED)
 *    - Maintain audit trail
 *    - Calculate P&L from trades
 *    - Order can be cancelled, trade cannot **/ 