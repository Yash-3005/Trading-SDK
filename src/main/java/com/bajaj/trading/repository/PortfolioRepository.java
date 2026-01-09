package com.bajaj.trading.repository;

import com.bajaj.trading.model.Instrument;
import com.bajaj.trading.model.Order;
import com.bajaj.trading.model.Portfolio;
import com.bajaj.trading.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    
    // Find all holdings for a user
    List<Portfolio> findByUserId(String userId);
    
    // Find specific stock holding for a user
    Optional<Portfolio> findByUserIdAndSymbol(String userId, String symbol);
    
    // Check if user has any shares of a stock
    boolean existsByUserIdAndSymbol(String userId, String symbol);
}