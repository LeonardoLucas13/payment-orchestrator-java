# Payment Orchestrator (Java)

REST API that creates orders safely, even when the client sends the same request more than once (double click, network retry). Built to practice patterns I used in production with payment gateways.

**Stack:** Java 21 · Spring Boot 4 · PostgreSQL 16 · Flyway · Docker

## Idempotency

Every `POST /orders` must send an `Idempotency-Key` header. The first request with a key is processed; repeated requests with the same key get the **same response**, and no duplicate order is created.

```
POST /orders + Idempotency-Key
│
├─ INSERT key ... ON CONFLICT DO NOTHING
│
├─ inserted 1 row → create order → save response → 201
│
└─ inserted 0 rows → load saved key
     ├─ different request body → 422
     ├─ still processing       → 409
     └─ completed              → return saved response
```

- The **primary key** on `idempotency_keys` decides the race between concurrent requests (no check-then-act).
- A **SHA-256 hash** of the body detects a key reused with a different request.
- If order creation fails, the key is released so the client can retry.

## Running locally

```bash
cp .env.example .env        # fill in your local values
docker compose up -d        # PostgreSQL on port 5433
./mvnw spring-boot:run      # API on port 8090
```

Flyway creates the schema on startup.

## Example

```bash
curl -X POST http://localhost:8090/orders \
  -H "Content-Type: application/json" \
  -H "Idempotency-Key: order-123" \
  -d '{"customerEmail":"client@example.com","amountCents":1000}'
```

Send it twice: the response (and the order `id`) is the same.

## Roadmap

- [x] Orders API with validation
- [x] Idempotency keys
- [ ] Automated tests (JUnit + Testcontainers)
- [ ] Payment gateways with Factory pattern
- [ ] Webhook processing with deduplication
- [ ] Deploy on AWS