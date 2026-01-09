package com.bajaj.trading;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application Class - Entry point of the Trading SDK
 */
@SpringBootApplication
public class TradingApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(TradingApplication.class, args);
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🚀 Trading SDK Application Started Successfully!");
        System.out.println("=".repeat(60));
        System.out.println("📊 Application running on: http://localhost:8080");
        System.out.println("📚 Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("🗄️  H2 Console: http://localhost:8080/h2-console");
        System.out.println("   JDBC URL: jdbc:h2:mem:tradingdb");
        System.out.println("   Username: sa");
        System.out.println("   Password: (leave empty)");
        System.out.println("=".repeat(60) + "\n");
    }
}

/**
 * INTERVIEW EXPLANATION:
 * 
 * Q: What is @SpringBootApplication?
 * A: Magic annotation that combines 3 annotations:
 *    1. @Configuration - Marks class as config source
 *    2. @EnableAutoConfiguration - Auto-configures Spring beans
 *    3. @ComponentScan - Scans for @Component, @Service, @Controller
 *    
 *    It's the "start here" marker for Spring Boot
 * 
 * Q: What does SpringApplication.run() do?
 * A: Starts the entire application:
 *    1. Creates application context
 *    2. Scans for components (@Controller, @Service, @Repository)
 *    3. Configures database, web server
 *    4. Starts embedded Tomcat server (default port 8080)
 *    5. Initializes all beans
 *    6. Makes application ready to handle requests
 * 
 * Q: Where does Spring Boot look for components?
 * A: In the package where main class is located and sub-packages
 *    
 *    If TradingApplication is in com.bajaj.trading
 *    It scans:
 *    - com.bajaj.trading
 *    - com.bajaj.trading.controller
 *    - com.bajaj.trading.service
 *    - com.bajaj.trading.repository
 *    etc.
 * 
 * Q: What happens when application starts?
 * A: Order of execution:
 *    1. main() method runs
 *    2. SpringApplication.run() starts Spring Boot
 *    3. Application context created
 *    4. Database connection established (H2)
 *    5. Entities become database tables
 *    6. Repositories initialized
 *    7. Services initialized
 *    8. Controllers registered
 *    9. @PostConstruct methods run (sample data loaded)
 *    10. Tomcat server starts
 *    11. Application ready to receive requests!
 * 
 * Q: What's the difference between Spring and Spring Boot?
 * A: Spring - Complex framework, manual configuration
 *    Spring Boot - Spring + Auto-configuration + Embedded server
 *    
 *    Spring requires XML config, tons of boilerplate
 *    Spring Boot: Just add @SpringBootApplication and run!
 * 
 * Q: Can I change the port from 8080?
 * A: Yes! In application.properties:
 *    server.port=9090
 */