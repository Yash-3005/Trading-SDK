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
@RestController
@RequestMapping("/api/v1/portfolio")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Portfolio", description = "APIs for viewing portfolio holdings")
class PortfolioController {
    
    private final PortfolioService portfolioService;
    
    /**
     * GET /api/v1/portfolio
     * Fetch user's complete portfolio
     */
    @GetMapping
    @Operation(summary = "Get portfolio", description = "Returns user's current stock holdings with live values")
    public ResponseEntity<List<Portfolio>> getPortfolio() {
        log.info("GET /api/v1/portfolio - Fetching portfolio");
        
        List<Portfolio> portfolio = portfolioService.getPortfolio();
        return ResponseEntity.ok(portfolio);
    }
    
    /**
     * GET /api/v1/portfolio/{symbol}
     * Get holdings for a specific stock
     */
    @GetMapping("/{symbol}")
    @Operation(summary = "Get holding by symbol", description = "Returns holdings for a specific stock")
    public ResponseEntity<Portfolio> getHoldingBySymbol(@PathVariable String symbol) {
        log.info("GET /api/v1/portfolio/{} - Fetching holding", symbol);
        
        Portfolio holding = portfolioService.getHoldingBySymbol(symbol);
        
        if (holding != null) {
            return ResponseEntity.ok(holding);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
