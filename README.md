# ShopEase

A full-stack e-commerce platform built with **Spring Boot 3**, **Spring Security** and **PostgreSQL**, with Razorpay payments, Twilio OTP verification and a complete admin dashboard.

> 🚧 **Status: actively being modernised.** The original Thymeleaf UI is being migrated to **React + TypeScript**, and the backend is being refactored toward a clean REST API with JWT authentication and structured logging. See the [Roadmap](#roadmap) for progress.

---

## Features

### Customers
- Registration and login with **Twilio OTP verification**, plus OTP-based password reset
- Product catalogue with categories, subcategories, search and product detail pages
- Cart, wishlist, "Buy now" and multi-item checkout
- **Razorpay** online payments
- Coupons, product offers and category offers applied at checkout
- **Wallet** with full transaction history
- Refer-and-earn referral programme
- Order history, order cancellation, and product reviews
- Multiple saved addresses

### Admins
- Product, category and subcategory management, with soft delete and restore
- Stock management
- Coupon and offer management
- Customer management
- Order management with status updates
- **Sales reports** with CSV export
- Product price history tracking

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Backend | Spring Boot 3.1, Spring Web, Spring Data JPA, Spring Security, Bean Validation, Actuator |
| Database | PostgreSQL 16 (via Docker Compose) |
| Integrations | Razorpay (payments), Twilio (SMS OTP) |
| Mapping | ModelMapper |
| UI (current) | Thymeleaf |
| UI (in progress) | React 19 + TypeScript |
| Build | Maven (wrapper included), npm |

---

## Project Structure

```
shop-ease/
├── backend/                     Spring Boot application
│   ├── docker-compose.yml       Local PostgreSQL
│   └── src/main/java/com/shop/ease/
│       ├── Controller/          MVC + REST controllers
│       ├── service/             Business logic (orders, payments, wallet, reports…)
│       ├── Repository/          Spring Data JPA repositories
│       ├── model/               JPA entities
│       ├── dto/                 Data transfer objects
│       ├── configuration/       Security and ModelMapper config
│       └── Util/                Enums and converters
└── frontend/shop-ease/          React + TypeScript client (in progress)
```

---

## Getting Started

### Prerequisites
- Java 17
- Docker (for PostgreSQL)
- Node.js 18+ (for the React frontend)
- Razorpay and Twilio **test** accounts (optional, needed for payments and OTP)

### 1. Configure environment variables

Copy the example file and fill in your own values:

```bash
cp .env.example .env
```

| Variable | Description |
|---|---|
| `DB_URL` | JDBC URL, e.g. `jdbc:postgresql://localhost:5432/shopease` |
| `DB_USERNAME` | Database user |
| `DB_PASSWORD` | Database password |
| `RAZORPAY_KEY_ID` | Razorpay key ID (test mode) |
| `RAZORPAY_KEY_SECRET` | Razorpay key secret (test mode) |
| `TWILIO_ACCOUNT_SID` | Twilio account SID |
| `TWILIO_AUTH_TOKEN` | Twilio auth token |
| `TWILIO_PHONE_NUMBER` | Twilio sender number |

> Never commit `.env` or real credentials. `.env` is already in `.gitignore`.

### 2. Start PostgreSQL

```bash
cd backend
docker compose --env-file ../.env up -d
```

### 3. Run the backend

```bash
cd backend
./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
```

The app starts at **http://localhost:9090**.

### 4. Run the React frontend (in progress)

```bash
cd frontend/shop-ease
npm install
npm start
```

---

## Roadmap

**Done**
- [x] Core e-commerce flows: catalogue, cart, checkout, orders, payments
- [x] Coupons, offers, wallet, referrals, reviews
- [x] Admin dashboard with sales reports and CSV export
- [x] Local PostgreSQL via Docker Compose
- [x] Removed Lombok in favour of explicit Java code
- [x] Refactored sales and reporting into a dedicated service layer with `ResponseEntity`-based endpoints

**In progress**
- [ ] Migrate the UI from Thymeleaf to React + TypeScript
- [ ] Move all credentials to environment variables

**Planned**
- [ ] Stateless JWT authentication with Spring Security
- [ ] Structured logging with request correlation IDs
- [ ] Global exception handling with consistent error responses
- [ ] Unit and integration tests (JUnit 5, Mockito, Testcontainers)
- [ ] OpenAPI / Swagger documentation
- [ ] CI pipeline with GitHub Actions
- [ ] Dockerise the full stack

---

## Screenshots

_Coming soon._

---

## Author

**Sidharth Ram** · Full Stack Engineer
[LinkedIn](https://www.linkedin.com/in/sidharth-ram-work/) · [GitHub](https://github.com/sacrop)
