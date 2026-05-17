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
