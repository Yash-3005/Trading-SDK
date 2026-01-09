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
public interface InstrumentRepository extends JpaRepository<Instrument, Long> {
    // JpaRepository gives us: save(), findAll(), findById(), delete(), etc.
    
    // Custom method: Find instrument by symbol
    Optional<Instrument> findBySymbol(String symbol);
    
    // Spring automatically implements this based on method name!
    // It generates SQL: SELECT * FROM instruments WHERE symbol = ?
}