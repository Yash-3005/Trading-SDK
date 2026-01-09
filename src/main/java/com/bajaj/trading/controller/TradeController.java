package com.bajaj.trading.controller;

import com.bajaj.trading.model.Portfolio;
import com.bajaj.trading.model.Trade;
import com.bajaj.trading.service.TradeService;
import com.bajaj.trading.service.PortfolioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ==================== TRADE CONTROLLER ====================
 */
@RestController
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Trades", description = "APIs for viewing executed trades")
public class TradeController {
    
    private final TradeService tradeService;
    
    /**
     * GET /api/v1/trades
     * Fetch all executed trades for the user
     */
    @GetMapping
    @Operation(summary = "Get all trades", description = "Returns list of all executed trades")
    public ResponseEntity<List<Trade>> getAllTrades() {
        log.info("GET /api/v1/trades - Fetching all trades");
        
        List<Trade> trades = tradeService.getAllTrades();
        return ResponseEntity.ok(trades);
    }
    
    /**
     * GET /api/v1/trades/order/{orderId}
     * Get trade for a specific order
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get trade by order ID", description = "Returns trade details for a specific order")
    public ResponseEntity<Trade> getTradeByOrderId(@PathVariable Long orderId) {
        log.info("GET /api/v1/trades/order/{} - Fetching trade", orderId);
        
        Trade trade = tradeService.getTradeByOrderId(orderId);
        
        if (trade != null) {
            return ResponseEntity.ok(trade);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
