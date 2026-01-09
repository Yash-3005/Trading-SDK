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

@Service
@RequiredArgsConstructor
@Slf4j
public
class PortfolioService {
    
    private final PortfolioRepository portfolioRepository;
    private final InstrumentRepository instrumentRepository;
    private static final String DEFAULT_USER_ID = "user123";
    
    /**
     * Get user's complete portfolio with current values
     */
    public List<Portfolio> getPortfolio() {
        log.info("Fetching portfolio for user: {}", DEFAULT_USER_ID);
        
        List<Portfolio> holdings = portfolioRepository.findByUserId(DEFAULT_USER_ID);
        
        // Update current values based on latest prices
        holdings.forEach(holding -> {
            instrumentRepository.findBySymbol(holding.getSymbol())
                .ifPresent(instrument -> {
                    holding.updateCurrentValue(instrument.getLastTradedPrice());
                });
        });
        
        // Save updated values
        portfolioRepository.saveAll(holdings);
        
        return holdings;
    }
    
    /**
     * Get holdings for a specific symbol
     */
    public Portfolio getHoldingBySymbol(String symbol) {
        return portfolioRepository.findByUserIdAndSymbol(DEFAULT_USER_ID, symbol)
            .orElse(null);
    }
    
    /**
     * Calculate total portfolio value
     */
    public BigDecimal getTotalPortfolioValue() {
        List<Portfolio> holdings = getPortfolio();
        
        return holdings.stream()
            .map(Portfolio::getCurrentValue)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

/* 
* ========== PORTFOLIO SERVICE ==========
 * 
 * Q: What does getPortfolio() do?
 * A: Returns user's current holdings with live values
 *    1. Fetch all holdings from database
 *    2. Update current values using latest market prices
 *    3. Save updated values
 *    4. Return to user
 * 
 * Q: Why update current values every time?
 * A: Stock prices change constantly
 *    We store averagePrice (what you paid)
 *    But currentValue (market worth) needs refreshing
 *    
 *    Example:
 *    - You bought 10 TCS at ₹3000 (stored)
 *    - Current price: ₹3500 (from Instrument table)
 *    - currentValue = 10 × 3500 = ₹35,000 (calculated)
 * 
 * Q: What is stream() and reduce()?
 * A: Modern Java way to process collections
 *    
 *    Old way:
 *    BigDecimal total = BigDecimal.ZERO;
 *    for (Portfolio p : holdings) {
 *        total = total.add(p.getCurrentValue());
 *    }
 *    
 *    New way (functional):
 *    holdings.stream()
 *        .map(Portfolio::getCurrentValue)      // Extract values
 *        .reduce(BigDecimal.ZERO, BigDecimal::add);  // Sum them
 *    
 *    More readable and concise!
 * 
 * Q: Why return null instead of throwing exception?
 * A: Design choice:
 *    - null = "not found" is expected behavior
 *    - Exception = unexpected error
 *    Controller can check if null and return 404
 */