# BarPal

Containerized Spring Boot + React + MySQL stack.

## Stack

- Backend: Spring Boot (Java 21)
- Frontend: React (served by Nginx)
- Database: MySQL 8
- Orchestration: Docker Compose

## Quick Start (Dev)

1. Copy `.env` and adjust if needed (default ports avoid conflicts with your other homelab stacks):
   - MySQL host port: 3310
   - Backend host port: 8080
   - Web host port: 3001
2. Build & start:

```bash
docker compose up -d --build
```

3. Access:
   - API: <http://localhost:8080>
   - Web: <http://localhost:3001>
   - MySQL: localhost:3310

Stop & remove:

```bash
docker compose down
```

## Production

Environment variables go into `.env.prod` (create/adjust; don't commit real secrets). Then:

```bash
docker compose -f docker-compose.prod.yml --env-file .env.prod up -d --build
```

## Healthchecks

MySQL uses `mysqladmin ping`. Backend optionally hits `/actuator/health` (add Spring Actuator dependency to enable). If not using Actuator yet, remove/adjust the HEALTHCHECK in the backend Dockerfile.

## Rebuilding Only One Service

```bash
docker compose build barpal-backend && docker compose up -d barpal-backend
```

## Cleaning Volumes (DESTROYS DATA!)

```bash
docker compose down -v
```

## Jenkins Pipeline

The `Jenkinsfile` builds and deploys using `docker-compose.prod.yml`. Provide an `.env.prod` on the Jenkins agent (or inject credentials) before running.

## Next Steps

- Add Spring Security & JWT
- Add Actuator & metrics
- Implement database migrations (Flyway or Liquibase) instead of `ddl-auto=update`
- Add CI test stage before image build

---
Happy shipping!

