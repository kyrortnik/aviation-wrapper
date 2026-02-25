# Aviation Wrapper

A Spring MVC (Java 21) service that acts as a thin “wrapper” around an external aviation data provider, exposing a simplified API and adding operational concerns like resilience, caching, and error handling.

## Goals

- **Stateless service**: easy to scale horizontally and safe to restart.
- **Resilience by default**: tolerate flaky/slow downstream dependencies.
- **Production-friendly**: predictable timeouts, clear logs, and integration tests that validate real wiring.

---

## Statelessness

This application is designed to be **stateless**:

- No server-side HTTP session is required to handle requests.

**Why it matters:** Stateless services are simpler to deploy, scale, and recover. If a node dies, traffic can be routed to another instance without user-visible impact.

---

## Resilience

The service is built to be resilient when calling external systems. Typical resilience mechanisms used in wrapper services include:

- **Timeouts**: fail fast instead of hanging threads indefinitely.
- **Retries (carefully)**: for transient failures only, with caps/backoff.
- **Circuit breaking**: stop hammering a failing dependency and recover gracefully.
- **Fallbacks**: return a controlled response (or error) when the upstream is unavailable.
- **Bulkheads / limits**: prevent a slow downstream from exhausting app resources.
- **Caching**: reduce repeated upstream calls for the same data, improving latency and survivability.

**Expected behavior under upstream issues:**  
The API should respond deterministically (clear HTTP status + message) rather than causing long waits, thread starvation, or random failures.

---

## Prerequisites

- **Java 21**
- **Docker**
- **Gradle**

---

## How to Build and Run
- from the root directory: docker-compose up -d

## How to Run Integration Tests
- from the root directory: ./gradlew test --tests "*SmokeIntegrationTest"
## API Documentation (local)
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- API Docs: http://localhost:8080/v3/api-docs

## AI tools used
- ChatGPT: for consultation on libraries and tech stack
- IntelliJ IDEA AI chat: to generate this README.md, integration test
