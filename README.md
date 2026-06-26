# Food Ordering — Microservices

Migrating a 2019 JSP + MySQL monolith ("The System – Online Food Ordering") into Spring Boot
microservices, **one database per service**. This repository starts the migration and grows
service by service.

## Current state

The first services have been extracted from the monolith as independent Spring Boot apps, each
with its own PostgreSQL database and Flyway-managed schema:

| Service | Port | Owns |
|---|---|---|
| `restaurant-service` | 8081 | restaurants & menu items |
| `user-service` | 8082 | user accounts (unified, role-based) |
| `order-service` | 8083 | cart, orders, order items |

## Layout

```
backend/    Spring Boot services  (open this folder in IntelliJ)
database/   the original legacy dump (database/legacy) + schema reference
```

## Run a service

Each service needs its own PostgreSQL database. Start all three containers (credentials match each service's `application.yml` defaults):

```bash
docker run --name restaurant-db -e POSTGRES_DB=restaurant_db \
  -e POSTGRES_USER=restaurant -e POSTGRES_PASSWORD=restaurant \
  -p 5432:5432 -d postgres:16-alpine

docker run --name user-db -e POSTGRES_DB=user_db \
  -e POSTGRES_USER=usersvc -e POSTGRES_PASSWORD=usersvc \
  -p 5433:5432 -d postgres:16-alpine

docker run --name order-db -e POSTGRES_DB=order_db \
  -e POSTGRES_USER=ordersvc -e POSTGRES_PASSWORD=ordersvc \
  -p 5434:5432 -d postgres:16-alpine
```

Then run each service in its own terminal — Flyway creates the schema and seeds data on startup:

```bash
cd backend/services/restaurant-service && mvn spring-boot:run   # http://localhost:8081
cd backend/services/user-service       && mvn spring-boot:run   # http://localhost:8082
cd backend/services/order-service      && mvn spring-boot:run   # http://localhost:8083
```

## Tech

Java 21 · Spring Boot 4.1 · PostgreSQL 16 · Flyway · Maven.
