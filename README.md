# ♟️ Chess Club API

A Spring Boot 3.4.x REST API for managing chess clubs and users, powered by PostgreSQL 17.5, and fully containerized using Docker Compose.

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-Build-green)
![Dockerized](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📦 Requirements

| Tool             | Version |
|------------------|---------|
| Java             | 21      |
| Maven            | 3.8+    |
| Docker           | 27.5+   |
| Docker Compose   | v2.x+   |


---

## 📚 Key Dependencies & Tools

This project leverages the following libraries and tooling:

### 📦 Libraries
- **Lombok** — reduces boilerplate for getters/setters/builders via annotations
- **MapStruct** — compile‑time mappings between DTOs and Entities
- **Apache Tika** — file type detection and content inspection for uploads

### 🧰 Developer & CI Tools
- **Checkstyle Plugin** — enforces [Google Java Style Guide](https://checkstyle.sourceforge.io/google_style.html)
- **JaCoCo** — test coverage analysis and reporting
- **OpenAPI / Swagger UI** — automatically generated API documentation

> Configuration file for Checkstyle is located at `config/checkstyle.xml`

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

---

## 🚀 Getting Started

### ✅ On Linux/macOS/WSL

Run the following in your terminal:
```bash
  ./start.sh
```

🔒 You may need to make the script executable first:
```bash
  chmod +x start.sh
```

### 🪟 On Windows (CMD or PowerShell)

Run the following in your terminal:

```cmd
  start.bat
```

### Or run it manually

```bash
  mvn clean package -DskipTests
  docker compose up --build
```
---

## 📘 API Documentation

## OpenAPI UI (Swagger) is available here:
🔗 http://localhost:8080/api/v1/v2/swagger-ui.html

For generate the OpenApi doc
```bash
  mvn springdoc-openapi:generate
```
> Configuration file for Checkstyle is located at `target/open-a`
---

## 🧪 Testing & Coverage

```bash
  mvn test
```