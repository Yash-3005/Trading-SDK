package com.bajaj.trading.service;

import com.bajaj.trading.model.Instrument;
import com.bajaj.trading.repository.InstrumentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Instrument Service - Handles all instrument-related business logic
 */
@Service
@RequiredArgsConstructor  // Lombok: Auto-creates constructor with required fields
@Slf4j  // Lombok: Adds logger (log.info(), log.error(), etc.)
public class InstrumentService {
    
    // Dependency Injection: Spring automatically provides this
    private final InstrumentRepository instrumentRepository;
    
    /**
     * Get all available instruments
     */
    public List<Instrument> getAllInstruments() {
        log.info("Fetching all instruments");
        return instrumentRepository.findAll();
    }
    
    /**
     * Get instrument by symbol
     */
    public Optional<Instrument> getInstrumentBySymbol(String symbol) {
        log.info("Fetching instrument: {}", symbol);
        return instrumentRepository.findBySymbol(symbol);
    }
    
    /**
     * Initialize sample instruments when application starts
     * This runs once when the app starts
     */
    @PostConstruct
    public void initializeInstruments() {
        // Check if instruments already exist
        if (instrumentRepository.count() > 0) {
            log.info("Instruments already initialized");
            return;
        }
        
        log.info("Initializing sample instruments...");
        
        // Create sample stocks
        List<Instrument> instruments = List.of(
            new Instrument("RELIANCE", "NSE", "EQUITY", new BigDecimal("2450.50")),
            new Instrument("TCS", "NSE", "EQUITY", new BigDecimal("3520.75")),
            new Instrument("INFY", "NSE", "EQUITY", new BigDecimal("1450.25")),
            new Instrument("HDFCBANK", "NSE", "EQUITY", new BigDecimal("1625.00")),
            new Instrument("ICICIBANK", "NSE", "EQUITY", new BigDecimal("975.50")),
            new Instrument("WIPRO", "NSE", "EQUITY", new BigDecimal("420.80")),
            new Instrument("BHARTIARTL", "NSE", "EQUITY", new BigDecimal("850.60")),
            new Instrument("ITC", "NSE", "EQUITY", new BigDecimal("425.30")),
            new Instrument("SBIN", "NSE", "EQUITY", new BigDecimal("580.45")),
            new Instrument("BAJFINANCE", "NSE", "EQUITY", new BigDecimal("6850.00"))
        );
        
        instrumentRepository.saveAll(instruments);
        log.info("Initialized {} instruments", instruments.size());
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is @Service annotation?
 * A: Marks this class as a service layer (business logic)
 *    Spring will:
 *    - Create a single instance (singleton)
 *    - Make it available for dependency injection
 *    - Enable transaction management
 * 
 * Q: What is Dependency Injection?
 * A: Instead of creating objects yourself:
 *    InstrumentRepository repo = new InstrumentRepository();  // BAD
 *    
 *    Spring creates and "injects" them for you:
 *    private final InstrumentRepository instrumentRepository;  // GOOD
 *    
 *    Benefits:
 *    - Loose coupling
 *    - Easy testing (can inject mock objects)
 *    - Spring manages lifecycle
 * 
 * Q: What does @RequiredArgsConstructor do?
 * A: Lombok generates a constructor for all 'final' fields
 *    
 *    Instead of writing:
 *    public InstrumentService(InstrumentRepository repo) {
 *        this.instrumentRepository = repo;
 *    }
 *    
 *    Lombok does it automatically!
 * 
 * Q: What is @PostConstruct?
 * A: Method runs ONCE after application starts
 *    Perfect for initialization tasks like loading sample data
 *    
 *    Lifecycle: App Start → Constructor → @PostConstruct → Ready
 * 
 * Q: Why use log.info() instead of System.out.println()?
 * A: Professional logging:
 *    - Can control log levels (DEBUG, INFO, ERROR)
 *    - Can write to files
 *    - Can disable in production
 *    - Includes timestamp, thread name
 *    - System.out is for debugging only
 * 
 * Q: Why return Optional<Instrument>?
 * A: Safe handling of "not found" cases
 *    Controller can check: if (result.isPresent()) { ... }
 */