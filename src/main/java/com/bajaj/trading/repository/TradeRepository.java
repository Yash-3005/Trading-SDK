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
public interface TradeRepository extends JpaRepository<Trade, Long> {
    
    // Find all trades for a user
    List<Trade> findByUserId(String userId);
    
    // Find trade by original order ID
    Optional<Trade> findByOrderId(Long orderId);
}