package com.bajaj.trading.controller;

import com.bajaj.trading.model.Instrument;
import com.bajaj.trading.service.InstrumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Instrument Controller - Handles instrument-related API requests
 * Base URL: /api/v1/instruments
 */
@RestController
@RequestMapping("/api/v1/instruments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Instruments", description = "APIs for managing tradable instruments")
public class InstrumentController {
    
    private final InstrumentService instrumentService;
    
    /**
     * GET /api/v1/instruments
     * Fetch all available instruments
     */
    @GetMapping
    @Operation(summary = "Get all instruments", description = "Returns list of all tradable instruments")
    public ResponseEntity<List<Instrument>> getAllInstruments() {
        log.info("GET /api/v1/instruments - Fetching all instruments");
        
        List<Instrument> instruments = instrumentService.getAllInstruments();
        
        return ResponseEntity.ok(instruments);
    }
    
    /**
     * GET /api/v1/instruments/{symbol}
     * Fetch specific instrument by symbol
     */
    @GetMapping("/{symbol}")
    @Operation(summary = "Get instrument by symbol", description = "Returns details of a specific instrument")
    public ResponseEntity<Instrument> getInstrumentBySymbol(@PathVariable String symbol) {
        log.info("GET /api/v1/instruments/{} - Fetching instrument", symbol);
        
        return instrumentService.getInstrumentBySymbol(symbol)
            .map(ResponseEntity::ok)  // If found, return 200 OK
            .orElse(ResponseEntity.notFound().build());  // If not found, return 404
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is @RestController?
 * A: Combination of @Controller + @ResponseBody
 *    Tells Spring: "This class handles HTTP requests and returns JSON"
 *    Every method automatically converts objects to JSON
 * 
 * Q: What is @RequestMapping?
 * A: Defines base URL path for all endpoints in this controller
 *    /api/v1/instruments → All methods here start with this path
 * 
 * Q: What is @GetMapping?
 * A: Handles HTTP GET requests
 *    @GetMapping → GET /api/v1/instruments
 *    @GetMapping("/{symbol}") → GET /api/v1/instruments/TCS
 * 
 * Q: What is @PathVariable?
 * A: Extracts value from URL path
 *    URL: /api/v1/instruments/TCS
 *    @PathVariable String symbol → symbol = "TCS"
 * 
 * Q: What is ResponseEntity?
 * A: Wrapper that lets you control HTTP response
 *    - Status code (200, 404, 500)
 *    - Headers
 *    - Body
 *    
 *    ResponseEntity.ok(data) → 200 OK with data
 *    ResponseEntity.notFound().build() → 404 Not Found
 *    ResponseEntity.badRequest() → 400 Bad Request
 * 
 * Q: Explain the line: .map(ResponseEntity::ok).orElse(...)
 * A: It's handling Optional elegantly
 *    
 *    If instrument found:
 *    Optional<Instrument> → .map() → ResponseEntity.ok(instrument)
 *    
 *    If not found:
 *    Empty Optional → .orElse() → ResponseEntity.notFound()
 *    
 *    Traditional way:
 *    Optional<Instrument> opt = service.get(symbol);
 *    if (opt.isPresent()) {
 *        return ResponseEntity.ok(opt.get());
 *    } else {
 *        return ResponseEntity.notFound().build();
 *    }
 * 
 * Q: What are those @Operation and @Tag annotations?
 * A: Swagger/OpenAPI documentation
 *    Automatically generates interactive API docs
 *    Visible at: http://localhost:8080/swagger-ui.html
 */