# Auto Repair Quote API

A RESTful API for managing vehicle repair quotes — quotes, jobs and parts — built with **Java 21** and
**Spring Boot 3.5**, using a **hexagonal (ports & adapters)** architecture.

---

## Quick start

### Prerequisites
- **Java 21** (the build targets 21 — make sure `JAVA_HOME` points to a JDK 21)
- **Maven 3.9+**

### Run the app
```bash
# from the project root
JAVA_HOME=/path/to/jdk-21 mvn spring-boot:run
```
The app starts on **http://localhost:8080** with **sample data preloaded** (two quotes, with jobs and a
mix of mechanical/fluid parts, a fixed-price job, and an unauthorised job).

Or build and run the jar:
```bash
JAVA_HOME=/path/to/jdk-21 mvn clean package
java -jar target/auto-repair-0.0.1-SNAPSHOT.jar
```

### Explore it
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI spec:** http://localhost:8080/v3/api-docs
- **H2 console:** http://localhost:8080/h2-console — JDBC URL `jdbc:h2:mem:autorepair`, user `sa`, no password
- **Health:** http://localhost:8080/actuator/health

### Run the tests
```bash
JAVA_HOME=/path/to/jdk-21 mvn test
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
- **Restraint over showmanship:** no Lombok, no MapStruct, no Spring Security, no HATEOAS — each is
  justifiable at scale, none here.

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
