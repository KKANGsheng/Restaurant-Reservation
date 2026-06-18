  # Restaurant Reservation System                                                                                                                                                                                                                                                                                                     
  ## Tech stack
  Java 21 · Spring Boot 3.2 · MySQL 8 · Redis 7 · Kafka · JWT · Docker

  ## Features
  - Multi-tenant: merchants own restaurants and customers
  - JWT auth with refresh tokens, role-based access (MERCHANT, CUSTOMER)
  - Reservation booking with Redis-backed idempotency (lock + 7-day debounce)
  - Event-driven notifications via Kafka → email
  - Invite-token user onboarding
  - Forgot-password flow with single-use, time-bound tokens

### API Endpoints Overview

#### Authentication & Authorization
```
POST   /api/v1/auth/user/register               - Register new user
POST   /api/v1/auth/user/login                  - User login
POST   /api/v1/auth/user/logout                 - User logout
POST   /api/v1/auth/user/refreshToken           - Refresh access token
POST   /api/v1/auth/password/reset              - Reset Password
POST   /api/v1/auth/password/forgot             - Forgot Password
POST   /api/v1/auth/password/reset-confirm      - Forgot Password Confirm
```

#### Merchant
```
POST    /api/v1/merchant/invite-link      - Generate Invite Link to user to register under merchant
GET    /api/v1/merchant/user              - Create User under merchant
```

#### Reservation
```
POST   /api/v1/reservation                     - Create Reservation
PUT    /api/v1/reservation/{id}/cancel         - Cancel Reservation
PUT    /api/v1/reservation/{id}/confirm        - Confirm Reservation
GET    /api/v1/reservation/                    - Get Reservation
GET    /api/v1/reservation/history             - Get Reservation History
```

#### Restaurant
```
POST   /api/v1/restaurant                          - Create Restaurant
GET    /api/v1/restaurant/getCustomerRestaurants   - Get All customer Restaurants
GET    /api/v1/restaurant/getAllRestaurants        - Get All restaurants
PUT    /api/v1/restaurant/{id}                     - Update Restaurant 
POST   /api/v1/restaurant/{id}                     - Delete Restaurant 
```

#### Table
```
POST   /api/v1/restaurant                          - Create Table
GET    /api/v1/table/{restaurantId}                - Get Restaurant Table
PUT    /api/v1/table/{tableId}                     - Update Table
PUT    /api/v1/table/{tableId}                     - Update Table 
PUT    /api/v1/table/{tableId}                     - Delete Restaurant 
```





```

