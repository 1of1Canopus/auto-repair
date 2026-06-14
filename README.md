# Auto Repair Quote API

A RESTful API for managing vehicle repair quotes — quotes, jobs and parts — built with **Java 21** and
**Spring Boot 3.5**, using a **hexagonal (ports & adapters)** architecture.

---

## Quick start

### Prerequisites
- **Java 21** — make sure `JAVA_HOME` points to a JDK 21. Maven is bundled via the wrapper (`./mvnw`).

### Run the app
```bash
# from the project root
./mvnw spring-boot:run
```
The app starts on **http://localhost:8080** with **sample data preloaded** (two quotes, with jobs and a
mix of mechanical/fluid parts, a fixed-price job, and an unauthorised job).

Or build and run the jar:
```bash
./mvnw clean package
java -jar target/auto-repair-0.0.1-SNAPSHOT.jar
```

Or with Docker:
```bash
docker build -t auto-repair .
docker run -p 8080:8080 auto-repair          # runs on in-memory H2
```

### Explore it
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI spec:** http://localhost:8080/v3/api-docs
- **H2 console:** http://localhost:8080/h2-console — JDBC URL `jdbc:h2:mem:autorepair`, user `sa`, no password
- **Health:** http://localhost:8080/actuator/health

### Run the tests
```bash
./mvnw test
```

### Try it (curl)
```bash
# 1. create a quote -> returns an "id"
curl -s -X POST localhost:8080/api/v1/quotes -H 'Content-Type: application/json' \
  -d '{"customerName":"Alice","customerEmail":"alice@example.com","vrm":"AB12CDE","vehicleDescription":"Audi A3","mileage":84000}'

# 2. add a job to that quote -> returns a job "id"
curl -s -X POST localhost:8080/api/v1/quotes/<quoteId>/jobs -H 'Content-Type: application/json' \
  -d '{"jobDescription":"Replace front brakes","labourTime":1.5,"labourRate":60.00,"customerAuthorized":true}'

# 3. add parts to the job (polymorphic by "type")
curl -s -X POST localhost:8080/api/v1/jobs/<jobId>/parts -H 'Content-Type: application/json' \
  -d '{"type":"MECHANICAL","partNumber":"BRK-001","partDescription":"Brake pad","quantity":2,"unitCost":45.00}'
curl -s -X POST localhost:8080/api/v1/jobs/<jobId>/parts -H 'Content-Type: application/json' \
  -d '{"type":"FLUID","partNumber":"OIL-001","partDescription":"Brake fluid","quantity":500,"unitCost":12.00}'

# 4. read it back with the computed total
curl -s localhost:8080/api/v1/quotes/<quoteId>
```

---

## API

| Method | Path | Description |
|---|---|---|
| `GET` | `/api/v1/quotes` | List all quotes |
| `GET` | `/api/v1/quotes/{id}` | Get a quote with its full hierarchy + computed total |
| `GET` | `/api/v1/quotes?customerEmail=…` | Filter quotes by customer email |
| `POST` | `/api/v1/quotes` | Create a quote |
| `POST` | `/api/v1/quotes/{quoteId}/jobs` | Add a job to a quote |
| `POST` | `/api/v1/jobs/{jobId}/parts` | Add a part (mechanical or fluid) to a job |

> `POST /quotes` is a deliberate extension beyond the brief — a quote has to originate somewhere, so it
> completes the lifecycle and gives the integration test a real entry point.

Parts are **polymorphic** — the request body carries a `type` discriminator:
```json
{ "type": "MECHANICAL", "partNumber": "BRK-001", "partDescription": "Brake pad", "quantity": 2, "unitCost": 45.00 }
{ "type": "FLUID",      "partNumber": "OIL-001", "partDescription": "Engine oil", "quantity": 500, "unitCost": 12.00 }
```
Errors are returned as RFC-7807 `application/problem+json`.

---

## Business rules
- **Job price:** `labourTime × labourRate + Σ(part costs)`, **or** a fixed overridden price that ignores
  both the time/rate and the parts.
- **Part cost:** mechanical = `quantity × unitCost`; fluid = `(quantity_ml / 1000) × unitCost_per_litre`,
  rounded to the cent.
- **Quote total:** sum of job prices for **authorised jobs only** — unauthorised jobs (and their parts)
  are excluded entirely.

---

## Architecture
Hexagonal, organised **by feature** (the `quote` bounded context), applied with restraint:

```
com.housedevinci.autorepair.quote
├── domain            immutable records with behaviour, zero framework imports
│                       Quote (aggregate root) · Job · Part (sealed: MechanicalPart | FluidPart)
├── application       QuoteService (use cases) + out-ports (QuoteRepository, JobCodeGenerator)
└── adapter
    ├── in/web        controllers · request/response DTOs · @ControllerAdvice (ProblemDetail)
    └── out/persistence   JPA entities (separate from the domain) · EntityMapper · Flyway schema
```

The key seam: **the domain model is separate from the JPA entities**, with a mapper bridging them at the
persistence boundary. The domain imports no Spring/JPA, so all business logic is tested in pure JUnit.

---

## Decisions & trade-offs
- **Hexagonal, but lean.** The brief framed this as the start of a larger system, so the domain sits
  behind ports — without per-use-case interface ceremony. Swapping persistence or adding a new entry
  point touches an adapter, not the domain.
- **Domain ≠ JPA entities.** Records can't cleanly be JPA entities (immutable, no no-arg constructor),
  and the domain must stay framework-free — so they're separate classes plus a mapper. The mapping cost
  is deliberate.
- **One aggregate, one repository.** Jobs and parts are never read on their own, so `Quote` is the only
  aggregate root and there is a single `QuoteRepository`.
- **`BigDecimal` for money**, never `double`.
- **H2 behind a port.** Zero-friction to run; moving to PostgreSQL is a new adapter + config with the
  domain untouched. Testcontainers would be the production test approach.
- **RFC-7807 `ProblemDetail`** for errors — built into Spring, no extra dependency.
- **Vendor-portable ids:** UUIDs are stored as `CHAR(36)` (one Hibernate setting), so the *same* schema
  boots on H2, MySQL **or** PostgreSQL — no native-`UUID` lock-in. `BINARY(16)` would be more compact but
  its DDL differs per vendor; portability wins here.
- **Transactions at the use case:** `@Transactional` lives on the application service (the unit of work),
  so each create/add is one atomic read-modify-write — not two separate transactions.
- **Restraint over showmanship:** no Lombok, no MapStruct, no Spring Security, no HATEOAS — each is
  justifiable at scale, none here.

## Deployment
- **Container:** multi-stage `Dockerfile` (JRE 21, non-root) — runs as-is on ECS Fargate.
- **Config:** a `prod` profile reads the datasource from env (`DB_URL`/`DB_USERNAME`/`DB_PASSWORD`) and
  switches `ddl-auto` to `validate` (Flyway owns the schema). The same image points at RDS by setting env
  vars: `SPRING_PROFILES_ACTIVE=prod` + the DB vars.
- **Health:** `/actuator/health` is the container health check.
- Sample data only loads under the local (default) profile — never in `prod` or tests.

## What I'd add given more time
- AuthN/AuthZ (OAuth2 resource server), pagination, idempotency keys on `POST`s
- Domain events / transactional outbox for downstream consumers
- Testcontainers + a PostgreSQL profile; observability (metrics, tracing)
- MapStruct once the mapping surface grows; automated dependency scanning (Dependabot)

---

## Testing
- **Domain:** many fast pure-JUnit tests (no Spring).
- **Application:** `QuoteService` against an in-memory fake repository.
- **Web:** `@WebMvcTest` slices per controller.
- **Persistence:** one `@SpringBootTest` save/reload round-trip on H2.
- **End-to-end:** one full HTTP happy-path — `POST /quotes` → `POST …/jobs` → `POST …/parts` → `GET`.

## Tech stack
Java 21 · Spring Boot 3.5.15 (web, validation, data-jpa, actuator) · Spring Data JPA / Hibernate · H2 ·
Flyway · springdoc-openapi · JUnit 5 / AssertJ / Mockito · Maven
