# API Usage Examples

## Quick Start Testing

### 1. Get All Instruments
```bash
curl -X GET http://localhost:8080/api/v1/instruments
```

### 2. Place Market BUY Order
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

### 3. Check Order Status (replace {orderId} with actual ID)
```bash
curl -X GET http://localhost:8080/api/v1/orders/1
```

### 4. View Trades
```bash
curl -X GET http://localhost:8080/api/v1/trades
```

### 5. View Portfolio
```bash
curl -X GET http://localhost:8080/api/v1/portfolio
```

---

## Complete Testing Workflow

### Step 1: Verify Instruments Are Loaded
```bash
# Get all instruments
curl http://localhost:8080/api/v1/instruments

# Get specific instrument
curl http://localhost:8080/api/v1/instruments/RELIANCE
```

**Output:**
```json
{
  "id": 1,
  "symbol": "RELIANCE",
  "exchange": "NSE",
  "instrumentType": "EQUITY",
  "lastTradedPrice": 2450.50
}
```

---

### Step 2: Place BUY Orders

#### MARKET Order (Executes Immediately)
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

#### LIMIT Order (Stays in PLACED status)
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "RELIANCE",
    "orderType": "BUY",
    "orderStyle": "LIMIT",
    "quantity": 5,
    "price": 2400.00
  }'
```

---

### Step 3: Buy Multiple Stocks
```bash
# Buy INFY
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "INFY",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": 20
  }'

# Buy HDFCBANK
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "HDFCBANK",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": 15
  }'
```

---

### Step 4: Check Portfolio
```bash
curl http://localhost:8080/api/v1/portfolio
```

**Expected Response:**
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
    "symbol": "INFY",
    "quantity": 20,
    "averagePrice": 1450.25,
    "currentValue": 29005.00
  },
  {
    "id": 3,
    "userId": "user123",
    "symbol": "HDFCBANK",
    "quantity": 15,
    "averagePrice": 1625.00,
    "currentValue": 24375.00
  }
]
```

---

### Step 5: Sell Shares
```bash
# Sell 5 shares of TCS
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "orderType": "SELL",
    "orderStyle": "MARKET",
    "quantity": 5
  }'
```

---

### Step 6: View All Orders
```bash
curl http://localhost:8080/api/v1/orders
```

---

### Step 7: View Trade History
```bash
curl http://localhost:8080/api/v1/trades
```

---

## Error Scenarios (Testing Validation)

### 1. Invalid Quantity (should fail)
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": -5
  }'
```

**Expected Error:**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Quantity must be greater than 0",
  "timestamp": "2026-01-09T03:30:00"
}
```

### 2. Invalid Symbol (should fail)
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "INVALID",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": 10
  }'
```

**Expected Error:**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Invalid instrument symbol: INVALID",
  "timestamp": "2026-01-09T03:30:00"
}
```

### 3. LIMIT Order Without Price (should fail)
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "orderType": "BUY",
    "orderStyle": "LIMIT",
    "quantity": 10
  }'
```

**Expected Error:**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Price must be specified for LIMIT orders",
  "timestamp": "2026-01-09T03:30:00"
}
```

### 4. Sell Without Holdings (should fail)
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "WIPRO",
    "orderType": "SELL",
    "orderStyle": "MARKET",
    "quantity": 10
  }'
```

**Expected Error:**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Cannot sell WIPRO: No holdings found",
  "timestamp": "2026-01-09T03:30:00"
}
```

### 5. Sell More Than Holdings (should fail)
```bash
# First buy 5 shares
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "ITC",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": 5
  }'

# Then try to sell 10 shares (more than owned)
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "ITC",
    "orderType": "SELL",
    "orderStyle": "MARKET",
    "quantity": 10
  }'
```

**Expected Error:**
```json
{
  "status": 400,
  "error": "Validation Error",
  "message": "Insufficient holdings. Available: 5, Requested: 10",
  "timestamp": "2026-01-09T03:30:00"
}
```

---

## Advanced: Testing Portfolio Average Price Calculation

### Scenario: Buy same stock at different prices

```bash
# Buy 10 TCS at ₹3520.75
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "symbol": "TCS",
    "orderType": "BUY",
    "orderStyle": "MARKET",
    "quantity": 10
  }'

# Manually update TCS price in database (via H2 console) to ₹3600
# Then buy 5 more TCS at ₹3600

# Check portfolio - average should be weighted average
curl http://localhost:8080/api/v1/portfolio/TCS
```

**Expected Calculation:**
```
Average Price = ((10 × 3520.75) + (5 × 3600)) / 15
             = (35207.50 + 18000) / 15
             = 53207.50 / 15
             = ₹3547.17
```

---

## PowerShell Commands (Windows Users)

If using PowerShell instead of bash/curl:

### Get Request
```powershell
Invoke-WebRequest -Uri http://localhost:8080/api/v1/instruments -Method GET
```

### Post Request
```powershell
$body = @{
    symbol = "TCS"
    orderType = "BUY"
    orderStyle = "MARKET"
    quantity = 10
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8080/api/v1/orders `
  -Method POST `
  -Body $body `
  -ContentType "application/json"
```

---

## Postman Collection Import

Create a new collection in Postman with these requests:

1. **GET** All Instruments: `http://localhost:8080/api/v1/instruments`
2. **POST** Place Order: `http://localhost:8080/api/v1/orders`
   - Body: Raw JSON
3. **GET** Orders: `http://localhost:8080/api/v1/orders`
4. **GET** Trades: `http://localhost:8080/api/v1/trades`
5. **GET** Portfolio: `http://localhost:8080/api/v1/portfolio`

---

## Browser Testing

### View Swagger UI
Open: http://localhost:8080/swagger-ui.html

### View H2 Database
Open: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:tradingdb`
- Username: `sa`
- Password: *(empty)*

Run SQL queries:
```sql
SELECT * FROM instruments;
SELECT * FROM orders;
SELECT * FROM trades;
SELECT * FROM portfolio;
```