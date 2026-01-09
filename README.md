# Trading SDK - Wrapper API for Stock Broking Platform

## 📋 Assignment Overview
This project is a **Wrapper SDK** around Trading APIs, simulating a stock broking platform similar to Zerodha Kite or Bajaj Broking. It provides REST APIs for managing instruments, orders, trades, and portfolio holdings.

## 🎯 Key Features
- ✅ View tradable financial instruments
- ✅ Place BUY/SELL orders (MARKET & LIMIT)
- ✅ Track order status with real-time execution
- ✅ View executed trades
- ✅ Fetch portfolio holdings with live values
- ✅ Automatic portfolio updates on trade execution
- ✅ Comprehensive validation and error handling
- ✅ Swagger API documentation
- ✅ In-memory H2 database for testing

## 🏗️ Architecture

### Project Structure
```
trading-sdk/
├── src/main/java/com/bajaj/trading/
│   ├── TradingApplication.java       # Main entry point
│   ├── controller/                   # REST API endpoints
│   │   ├── InstrumentController.java
│   │   ├── OrderController.java
│   │   ├── TradeController.java
│   │   └── PortfolioController.java
│   ├── service/                      # Business logic
│   │   ├── InstrumentService.java
│   │   ├── OrderService.java
│   │   ├── TradeService.java
│   │   └── PortfolioService.java
│   ├── repository/                   # Database access
│   │   ├── InstrumentRepository.java
│   │   ├── OrderRepository.java
│   │   ├── TradeRepository.java
│   │   └── PortfolioRepository.java
│   ├── model/                        # Data entities
│   │   ├── Instrument.java
│   │   ├── Order.java
│   │   ├── Trade.java
│   │   └── Portfolio.java
│   └── exception/                    # Error handling
│       └── GlobalExceptionHandler.java
└── src/main/resources/
    └── application.properties        # Configuration
```

### Technology Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.2.0
- **Database:** H2 (in-memory)
- **Build Tool:** Maven
- **API Documentation:** Swagger/OpenAPI
- **Libraries:** Lombok, Spring Data JPA, Validation

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Installation & Running

1. **Clone or download the project**
```bash
cd trading-sdk
```

2. **Build the project**
```bash
mvn clean install
```

3. **Run the application**
```bash
mvn spring-boot:run
```

Or run the JAR directly:
```bash
java -jar target/trading-sdk-1.0.0.jar
```

### Application URLs
- **Base API:** http://localhost:8080
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:tradingdb`
  - Username: `sa`
  - Password: *(leave empty)*

## 📚 API Documentation

### 1️⃣ Instrument APIs

#### Get All Instruments
```bash
GET /api/v1/instruments
```

**Response:**
```json
[
  {
    "id": 1,
    "symbol": "RELIANCE",
    "exchange": "NSE",
    "instrumentType": "EQUITY",
    "lastTradedPrice": 2450.50
  },
  {
    "id": 2,
    "symbol": "TCS",
    "exchange": "NSE",
    "instrumentType": "EQUITY",
    "lastTradedPrice": 3520.75
  }
]
```

#### Get Instrument by Symbol
```bash
GET /api/v1/instruments/TCS
```

### 2️⃣ Order APIs

#### Place Order (BUY - MARKET)
```bash
POST /api/v1/orders
Content-Type: application/json

{
  "symbol": "TCS",
  "orderType": "BUY",
  "orderStyle": "MARKET",
  "quantity": 10
}
```

**Response:**
```json
{
  "orderId": 1,
  "symbol": "TCS",
  "orderType": "BUY",
  "orderStyle": "MARKET",
  "quantity": 10,
  "price": null,
  "status": "EXECUTED",
  "createdAt": "2026-01-09T03:30:00",
  "executedAt": "2026-01-09T03:30:01",
  "userId": "user123"
}
```

#### Place Order (BUY - LIMIT)
```bash
POST /api/v1/orders
Content-Type: application/json

{
  "symbol": "RELIANCE",
  "orderType": "BUY",
  "orderStyle": "LIMIT",
  "quantity": 5,
  "price": 2400.00
}
```

#### Place Order (SELL)
```bash
POST /api/v1/orders
Content-Type: application/json

{
  "symbol": "TCS",
  "orderType": "SELL",
  "orderStyle": "MARKET",
  "quantity": 5
}
```

#### Get Order Status
```bash
GET /api/v1/orders/1
```

#### Get All Orders
```bash
GET /api/v1/orders
```

### 3️⃣ Trade APIs

#### Get All Trades
```bash
GET /api/v1/trades
```

**Response:**
```json
[
  {
    "tradeId": 1,
    "orderId": 1,
    "symbol": "TCS",
    "tradeType": "BUY",
    "quantity": 10,
    "executedPrice": 3520.75,
    "totalValue": 35207.50,
    "executedAt": "2026-01-09T03:30:01",
    "userId": "user123"
  }
]
```

### 4️⃣ Portfolio APIs

#### Get Portfolio
```bash
GET /api/v1/portfolio
```

**Response:**
```json
[
  {
    "id": 1,
    "userId": "user123",
    "symbol": "TCS",
    "quantity": 10,
    "averagePrice": 3520.75,
    "currentValue": 35207.50
  },
  {
    "id": 2,
    "userId": "user123",
    "symbol": "RELIANCE",
    "quantity": 5,
    "averagePrice": 2450.50,
    "currentValue": 12252.50
  }
]
```

## 🔄 Trading Flow Example

### Scenario: Buy TCS shares and then sell some

**Step 1: Check available instruments**
```bash
curl http://localhost:8080/api/v1/instruments/TCS
```

**Step 2: Place BUY order**
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": 10
  }'
```

**Step 3: Check order status**
```bash
curl http://localhost:8080/api/v1/orders/1
```

**Step 4: View executed trade**
```bash
curl http://localhost:8080/api/v1/trades
```

**Step 5: Check portfolio**
```bash
curl http://localhost:8080/api/v1/portfolio
```

**Step 6: Sell some shares**
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "orderType": "SELL",
    "orderStyle": "MARKET",
    "quantity": 5
  }'
```

## 🎯 Key Implementation Details

### Order Execution Logic
- **MARKET orders:** Execute immediately at current market price
- **LIMIT orders:** Stay in PLACED status (waiting for price condition)

### Portfolio Management
- **BUY:** Adds shares, calculates weighted average price
- **SELL:** Removes shares, validates sufficient holdings
- **Current Value:** Updates based on latest market prices

### Validations
✅ Quantity must be greater than 0  
✅ Symbol must exist in instruments  
✅ LIMIT orders require price  
✅ SELL orders check sufficient holdings  
✅ Proper error messages for all validation failures  

### Error Handling
- **400 Bad Request:** Invalid input, validation errors
- **404 Not Found:** Resource doesn't exist
- **500 Internal Server Error:** Unexpected errors

## 🧪 Testing with Swagger UI

Visit http://localhost:8080/swagger-ui.html for interactive API testing:
1. Expand any API endpoint
2. Click "Try it out"
3. Enter request parameters
4. Click "Execute"
5. View response

## 💡 Assumptions Made
1. Single hardcoded user (`user123`) - no authentication required
2. 10 pre-populated sample instruments
3. MARKET orders execute immediately at last traded price
4. LIMIT orders stay in PLACED status (execution logic not implemented)
5. No real market connectivity or price updates
6. In-memory database (data lost on restart)
7. All prices are in INR (₹)

## 🌟 Bonus Features Implemented
✅ Swagger/OpenAPI documentation  
✅ Comprehensive logging with SLF4J  
✅ Global exception handling  
✅ H2 console for database inspection  
✅ Clean code structure with Lombok  
✅ Proper HTTP status codes  

## 📊 Database Schema

### Tables
- **instruments:** Tradable stocks
- **orders:** User orders (NEW/PLACED/EXECUTED/CANCELLED)
- **trades:** Executed transactions
- **portfolio:** Current user holdings

### Sample Data
The application automatically loads 10 sample instruments on startup:
RELIANCE, TCS, INFY, HDFCBANK, ICICIBANK, WIPRO, BHARTIARTL, ITC, SBIN, BAJFINANCE

## 🔍 Troubleshooting

### Port 8080 already in use
Change port in `application.properties`:
```properties
server.port=9090
```

### Database connection errors
H2 is in-memory - no external database needed. Just ensure application starts successfully.

### Cannot execute LIMIT orders
By design, LIMIT orders stay in PLACED status. Only MARKET orders auto-execute.

## 👨‍💻 Developer

**Created for:** Bajaj Broking - Software Engineer Trainee Assignment  
**Date:** January 2026  

---

## 📞 Support
For questions or issues, please refer to the API documentation at `/swagger-ui.html` or check the logs in the console.