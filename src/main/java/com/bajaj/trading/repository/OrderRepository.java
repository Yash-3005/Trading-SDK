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
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    // Find all orders for a specific user
    List<Order> findByUserId(String userId);
    
    // Find orders by status
    List<Order> findByStatus(Order.OrderStatus status);
    
    // Find user's orders with specific status
    List<Order> findByUserIdAndStatus(String userId, Order.OrderStatus status);
}