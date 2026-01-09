package com.bajaj.trading.service;

import com.bajaj.trading.model.Instrument;
import com.bajaj.trading.model.Order;
import com.bajaj.trading.model.Portfolio;
import com.bajaj.trading.model.Trade;
import com.bajaj.trading.repository.InstrumentRepository;
import com.bajaj.trading.repository.OrderRepository;
import com.bajaj.trading.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InstrumentRepository instrumentRepository;
    private final PortfolioRepository portfolioRepository;
    private final TradeService tradeService;
    
    // Hardcoded user for this assignment
    private static final String DEFAULT_USER_ID = "user123";
    
    /**
     * Place a new order with validations
     */
    @Transactional  // Ensures all database operations succeed or rollback together
    public Order placeOrder(Order orderRequest) {
        log.info("Placing order: {} {} {} shares at {}", 
                orderRequest.getOrderType(), orderRequest.getSymbol(), 
                orderRequest.getQuantity(), orderRequest.getPrice());
        
        // ========== VALIDATIONS ==========
        
        // 1. Validate quantity
        if (orderRequest.getQuantity() == null || orderRequest.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        
        // 2. Validate instrument exists
        Optional<Instrument> instrument = instrumentRepository.findBySymbol(orderRequest.getSymbol());
        if (instrument.isEmpty()) {
            throw new IllegalArgumentException("Invalid instrument symbol: " + orderRequest.getSymbol());
        }
        
        // 3. Validate price for LIMIT orders
        if (orderRequest.getOrderStyle() == Order.OrderStyle.LIMIT) {
            if (orderRequest.getPrice() == null || orderRequest.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Price must be specified for LIMIT orders");
            }
        }
        
        // 4. For SELL orders, check if user has sufficient quantity
        if (orderRequest.getOrderType() == Order.OrderType.SELL) {
            validateSufficientHoldings(orderRequest.getSymbol(), orderRequest.getQuantity());
        }
        
        // ========== CREATE ORDER ==========
        Order order = new Order();
        order.setSymbol(orderRequest.getSymbol());
        order.setOrderType(orderRequest.getOrderType());
        order.setOrderStyle(orderRequest.getOrderStyle());
        order.setQuantity(orderRequest.getQuantity());
        order.setPrice(orderRequest.getPrice());
        order.setStatus(Order.OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());
        order.setUserId(DEFAULT_USER_ID);
        
        // Save order
        order = orderRepository.save(order);
        log.info("Order created with ID: {}", order.getOrderId());
        
        // ========== AUTO-EXECUTE MARKET ORDERS ==========
        if (order.getOrderStyle() == Order.OrderStyle.MARKET) {
            executeOrder(order, instrument.get());
        } else {
            // LIMIT orders stay in PLACED status
            order.setStatus(Order.OrderStatus.PLACED);
            orderRepository.save(order);
            log.info("LIMIT order placed, waiting for execution");
        }
        
        return order;
    }
    
    /**
     * Execute an order (called for MARKET orders)
     */
    private void executeOrder(Order order, Instrument instrument) {
        log.info("Executing order: {}", order.getOrderId());
        
        // Get execution price
        BigDecimal executionPrice = instrument.getLastTradedPrice();
        
        // Update order status
        order.setStatus(Order.OrderStatus.EXECUTED);
        order.setExecutedAt(LocalDateTime.now());
        orderRepository.save(order);
        
        // Create trade record
        Trade trade = tradeService.createTrade(order, executionPrice);
        log.info("Trade created: {}", trade.getTradeId());
        
        // Update portfolio
        updatePortfolio(order, executionPrice);
        
        log.info("Order {} executed successfully", order.getOrderId());
    }
    
    /**
     * Update user's portfolio after order execution
     */
    private void updatePortfolio(Order order, BigDecimal executionPrice) {
        String userId = order.getUserId();
        String symbol = order.getSymbol();
        
        Optional<Portfolio> existingHolding = portfolioRepository.findByUserIdAndSymbol(userId, symbol);
        
        if (order.getOrderType() == Order.OrderType.BUY) {
            // BUY: Add shares to portfolio
            if (existingHolding.isPresent()) {
                Portfolio portfolio = existingHolding.get();
                portfolio.addShares(order.getQuantity(), executionPrice);
                portfolioRepository.save(portfolio);
                log.info("Updated portfolio: {} shares of {}", portfolio.getQuantity(), symbol);
            } else {
                // Create new portfolio entry
                Portfolio portfolio = new Portfolio(userId, symbol, order.getQuantity(), executionPrice);
                portfolio.updateCurrentValue(executionPrice);
                portfolioRepository.save(portfolio);
                log.info("Created new portfolio entry for {}", symbol);
            }
        } else {
            // SELL: Remove shares from portfolio
            if (existingHolding.isPresent()) {
                Portfolio portfolio = existingHolding.get();
                portfolio.removeShares(order.getQuantity());
                
                // If all shares sold, delete portfolio entry
                if (portfolio.getQuantity() == 0) {
                    portfolioRepository.delete(portfolio);
                    log.info("Removed {} from portfolio (sold all shares)", symbol);
                } else {
                    portfolioRepository.save(portfolio);
                    log.info("Updated portfolio: {} shares remaining of {}", portfolio.getQuantity(), symbol);
                }
            }
        }
    }
    
    /**
     * Validate user has sufficient shares for SELL order
     */
    private void validateSufficientHoldings(String symbol, Integer quantityToSell) {
        Optional<Portfolio> holding = portfolioRepository.findByUserIdAndSymbol(DEFAULT_USER_ID, symbol);
        
        if (holding.isEmpty()) {
            throw new IllegalArgumentException("Cannot sell " + symbol + ": No holdings found");
        }
        
        if (holding.get().getQuantity() < quantityToSell) {
            throw new IllegalArgumentException(
                String.format("Insufficient holdings. Available: %d, Requested: %d", 
                    holding.get().getQuantity(), quantityToSell)
            );
        }
    }
    
    /**
     * Get order by ID
     */
    public Optional<Order> getOrderById(Long orderId) {
        return orderRepository.findById(orderId);
    }
    
    /**
     * Get all orders for the user
     */
    public List<Order> getAllOrders() {
        return orderRepository.findByUserId(DEFAULT_USER_ID);
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is @Transactional?
 * A: Ensures database consistency
 *    If ANY operation fails, ALL operations rollback
 *    
 *    Example: Place order + Execute + Update portfolio
 *    If portfolio update fails → entire operation rolls back
 *    This prevents partial data (order exists but portfolio not updated)
 * 
 * Q: Walk me through the order flow
 * A: 1. User submits order → Validate (quantity, symbol, price)
 *    2. Create Order entity → Save to database (status: NEW)
 *    3. If MARKET order:
 *       a. Get current price from Instrument
 *       b. Execute immediately (status: EXECUTED)
 *       c. Create Trade record
 *       d. Update Portfolio (add/remove shares)
 *    4. If LIMIT order:
 *       - Status: PLACED (wait for price condition)
 * 
 * Q: Why validate SELL orders have sufficient holdings?
 * A: You can't sell what you don't own!
 *    Check portfolio before allowing sell
 *    Prevents negative holdings
 * 
 * Q: What's the difference in portfolio update for BUY vs SELL?
 * A: BUY: Add shares, calculate new average price
 *    SELL: Remove shares, keep average price same
 *    If quantity reaches 0 → delete portfolio entry
 * 
 * Q: Why use BigDecimal for prices?
 * A: Financial precision! 
 *    double has rounding errors
 *    BigDecimal gives exact decimal arithmetic
 */