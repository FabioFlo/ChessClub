# ♟️ Chess Club API

A Spring Boot 3.4.x REST API for managing chess clubs and users, powered by PostgreSQL 17.5, and fully containerized using Docker Compose.

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-Build-success-green)
![Dockerized](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📦 Requirements

| Tool             | Version       |
|------------------|---------------|
| Java             | 21            |
| Maven            | 3.9+          |
| Docker           | 20.10+        |
| Docker Compose   | v2.x+         |

---

## ⚙️ Configuration

All environment variables are defined in the provided `.env` file:

```env
DB_NAME=mydb
DB_USER=myuser
DB_PASS=mypassword
SERVER_PORT=8080
SPRING_PROFILES_ACTIVE=local
```
⚠️ These are demo values only. Never use them in production.

