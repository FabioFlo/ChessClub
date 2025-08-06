# ♟️ Chess Club API

A Spring Boot 3.4.x REST API for managing chess clubs and users, powered by PostgreSQL 17.5, and fully containerized
using Docker Compose.

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-Build-green)
![Dockerized](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 📦 Requirements

| Tool           | Version |
|----------------|---------|
| Java           | 21      |
| Maven          | 3.8+    |
| Docker         | 27.5+   |
| Docker Compose | v2.x+   |

---

## 🐳 Docker Troubleshooting (Linux / WSL)

> If you encounter issues with `docker compose` not working or returning permission errors, follow these steps.

### ✅ Check if Docker is installed properly

```bash
  docker --version
  docker compose version
```

If you get an error like:

```
docker: unknown command: compose
```

### 🔧 Fix: Install Docker Compose plugin (APT)

```bash
  sudo apt update
  sudo apt install docker-compose-plugin
```

### 🧰 Fix: Docker daemon not running

If you see:

```
Cannot connect to the Docker daemon at unix:///var/run/docker.sock
```

Start the Docker daemon:

```bash
  sudo systemctl start docker
  sudo systemctl enable docker  # Optional: start Docker at boot
```

### 🧹 Fix: Broken or conflicting plugin

If you see something like:

```
WARNING: Plugin ".../docker-compose" is not valid: permission denied
```

Remove the broken override:

```bash
  rm ~/.docker/cli-plugins/docker-compose
```

Then retry:

```bash
  docker compose version
```

✅ You should now see something like:

```
Docker Compose version v2.39.1
```

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

___

## 🛑 Stopping the Application

### ✅ Linux/macOS/WSL

```bash
  ./stop.sh
```

🔒 You may need to make the script executable first:

```bash
  chmod +x start.sh
```

### 🪟 On Windows (CMD or PowerShell)

Run the following in your terminal:

```cmd
  stop.bat
```

### 🔁 Full Reset (Wipe Database)

```bash
  ./stop-clean.sh
# or
  stop-clean.bat
```

---

## 📘 API Documentation

### OpenAPI UI (Swagger) is available here:

🔗 http://localhost:8080/api/v1/swagger-ui.html

To generate the OpenApi json:
> ⚠️ The app needs to be up and running!

```bash
  mvn springdoc-openapi:generate
```

> OpenApi json is located at `target/openapi.json`

---

## 🧪 Testing & Coverage

To generate the test coverage:

```bash
  mvn clean verify
```

> Jacoco index.html is located at `target/site/jacoco/index.html`