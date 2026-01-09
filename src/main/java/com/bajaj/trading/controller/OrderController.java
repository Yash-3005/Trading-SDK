package com.bajaj.trading.controller;

import com.bajaj.trading.model.Order;
import com.bajaj.trading.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Orders", description = "APIs for order management")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping
    @Operation(summary = "Place new order", description = "Creates and executes a new buy/sell order")
    public ResponseEntity<?> placeOrder(@Valid @RequestBody Order orderRequest) {
        log.info("POST /api/v1/orders - Placing order: {}", orderRequest);
        
        try {
            Order placedOrder = orderService.placeOrder(orderRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(placedOrder);
        } catch (IllegalArgumentException e) {
            log.error("Order validation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            log.error("Error placing order", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to place order"));
        }
    }
    
    @GetMapping("/{orderId}")
    @Operation(summary = "Get order by ID", description = "Returns order details and status")
    public ResponseEntity<?> getOrderById(@PathVariable Long orderId) {
        log.info("GET /api/v1/orders/{} - Fetching order", orderId);
        return orderService.getOrderById(orderId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    
    @GetMapping
    @Operation(summary = "Get all orders", description = "Returns list of all orders for the user")
    public ResponseEntity<List<Order>> getAllOrders() {
        log.info("GET /api/v1/orders - Fetching all orders");
        List<Order> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }
}