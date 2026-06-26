# Changelog

All notable changes to this project are recorded here, **newest first**. Each entry maps to one
commit / milestone in the legacy → microservices migration, so you can see exactly what a given
step delivered — and why — without reading the git diff.

Format based on [Keep a Changelog](https://keepachangelog.com/); version numbers track the
migration phases rather than a released product.

## [Unreleased]

_The next migration phase will be recorded here as it lands._

## [0.1.0] — 2026-06-25 — CRUD baseline

The starting point of the migration: the first slices of the 2019 JSP + MySQL monolith carved out
as independent Spring Boot services, each owning its **own PostgreSQL database** with a
Flyway-managed schema. No inter-service calls, gateway, messaging, or security yet — deliberately
simple, so later phases have something concrete to build on.

### Added

- **restaurant-service** — port 8081, database `restaurant_db`. REST CRUD for restaurants and their
  menu items (`/api/restaurants`, `/api/restaurants/{id}/menu`, `/api/menu-items/...`).
- **user-service** — port 8082, database `user_db`. User accounts, with the legacy
  `admin` / `customer` / `restaurant` login tables unified into a single `users` table plus a
  `role` column. Passwords stored as **BCrypt** hashes; three demo users seeded on first start.
- **order-service** — port 8083, database `order_db`. Cart management, **server-side price
  calculation** (the client only sends item ids + quantities — never prices), and order placement
  that freezes item name/price as **immutable line snapshots**. Prices come from a local
  `menu_item_ref` copy so checkout makes no cross-service call.
- Consistent layering in every service: `controller / service / repository / entity / dto / exception`.
- Entities use **Lombok** (`@Getter`/`@Setter`, no `@Data` on JPA entities); request/response
  **DTOs are Java records**; input validated with Jakarta Bean Validation (`@Valid`).
- Centralized error handling via `@RestControllerAdvice` returning RFC 9457 `ProblemDetail`
  responses (404 not-found, 409 duplicate email, 400 validation/invalid-order) — no stack traces leak.
- **JPA auditing**: `created_at` / `updated_at` populated automatically on the core entities.
- **Database-per-service** with a Flyway `V1__init.sql` in each service that creates the schema and
  seeds data migrated from the legacy MySQL dump.
- Postman collection covering all three services for manual testing.
- The original legacy database dump preserved at `database/legacy/dumpsqlOSP.sql` as the "before",
  with `database/schema-reference.md` documenting the new per-service schema.

### Design notes (the "why")

- **One database per service** — no cross-service database joins; references such as
  `restaurant_id`, `user_id`, and `menu_item_id` are *logical* only. This is the defining rule the
  rest of the architecture builds on.
- **Server-computed pricing + snapshots** — the server never trusts a client-supplied price, and a
  later menu price change never rewrites past orders.
- **Security improvements over the monolith** — BCrypt instead of plaintext passwords, and
  parameterized JPA queries instead of the legacy string-concatenated SQL.

---

_Once the repository is pushed, each version heading can link to its git tag / compare view._
