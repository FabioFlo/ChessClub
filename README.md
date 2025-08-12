# ♟️ Chess Club API

A Spring Boot 3.4.x REST API for managing chess clubs, players, tournaments, and games.  
It provides endpoints to register users, manage memberships, schedule tournaments, and record
results.  
Powered by PostgreSQL 17.5, and fully containerized using Docker Compose.

![Java](https://img.shields.io/badge/Java-21-blue)
![Maven](https://img.shields.io/badge/Maven-Build-green)
![Dockerized](https://img.shields.io/badge/Docker-Ready-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

## 🎯 Project Overview & Personal Notes

### 🔍 What This Project Demonstrates

As a portfolio piece for GitHub and LinkedIn, this project highlights my proficiency in:

- Modern Java Development — Spring Boot 3.4.x with Java 21 features
- Clean Architecture — Well-structured layers with DTO/Entity separation using MapStruct
- Database Design — PostgreSQL integration with proper relationship modeling
- DevOps Practices — Full Docker containerization and automated deployment scripts
- Code Quality — Comprehensive testing, coverage reporting (JaCoCo), and style enforcement (
  Checkstyle)
- API Documentation — Auto-generated OpenAPI/Swagger documentation
- Security Implementation — Role-based access control and authentication

### 💭 Technical Decisions & Learnings

This project allowed me to explore several interesting technical challenges:

- Role-Based Access Control — Implementing a flexible permission system that scales from individual
  users to admin-level
  operations
- File Upload Handling — Using Apache Tika for secure file type detection and validation
  Database Seeding Strategy — Creating configurable data initialization for development and demo
  purposes
- Cross-Platform Deployment — Ensuring seamless setup across Windows, Linux, and macOS environments

### 🎓 Why Chess Club Management?

I chose this domain because I am a chess player too and this topic is more familiar to me and
naturally encompasses many
real-world application requirements: user management, tournament brackets, game results tracking. It's complex enough to
showcase advanced
development patterns
while being intuitive for anyone reviewing the code.

When I started this project, I had almost zero experience with unit and integration tests and the
various testing tools I've used. Because of this, I decided to build it following the Test-Driven
Development (TDD) practice.

My testing journey began with an Udemy course and continued with a lot of practice and questions
about best practices to a friend who has far more experience than I do—and I'm not talking about AI!

But let's be honest, I obviously got some help from AI as well, just like every developer nowadays.
So, how did I use this tool?

I don't like to just copy and paste code from a chatbot without understanding what I'm doing. Here's
how I proceeded:

- I first thought about the project, how I wanted it to work, and researched real-world examples
  online.

- Then, I created a roadmap and outlined the logic of the app, including classes, some features, and
  the database. I only questioned the AI afterward with prompts like, "This is my idea; what is the
  best practice?" Then I thought about the answers it gave me and asked follow-up questions to
  refine the result until I was satisfied. Sometimes, I specifically asked the AI not to write code,
  but simply to give me steps to follow based on best practices.

More features could be added such as `Gallery` with photos of `Tournaments`, `Email service`,
`Cache management`, but for now I have decided to keep it simple and eventually implement them later.

### What about actual code problems?

I usually try to solve them myself first. Afterward, I ask the AI for improvements, other times it
was useful to me in
solving problems that required the use of Java classes and methods that I didn't know at all and
this gave me the
opportunity to learn new aspects of the language.

Sometimes the AI helped me discover new tools and topics to study. This led me to question my own
work and think about
how to improve it. I used the AI as a more expert developer, guiding me through best practices (
exactly what I did with
my friend—thank you, D.!).

### In conclusion

AI can be very useful, but it needs clear direction to be efficient. The more specific my idea or
problem was, the
better the answer I received, and the more I learned and understood.

---

## 📑 Table of Contents

- [Requirements](#-requirements-and-installation-help)
- [Docker Troubleshooting (Linux/WSL)](#-docker-troubleshooting-linux--wsl)
- [Key Dependencies & Tools](#-key-dependencies--tools)
- [Configuration](#-configuration)
- [Getting Started](#-getting-started)
- [Stopping the Application](#-stopping-the-application)
- [API Documentation](#-api-documentation)
- [Testing & Coverage](#-testing--coverage)
- [License](#-license)
- [Code Style & Formatting](#-code-style--formatting)
- [Author](#-author)

---

## 📦 Requirements (and Installation Help)

To run the project using the provided scripts (`start.sh`, `start.bat`), you only need the following
installed and
configured on your system:

| Tool       | Purpose                                       | Required? |
|------------|-----------------------------------------------|-----------|
| **Docker** | Runs the containers via `docker compose`      | ✅ Yes     |
| **Maven**  | Builds the Java project (`mvn clean package`) | ✅ Yes     |

> ✅ All other dependencies (Java, PostgreSQL, etc.) are managed inside Docker containers.  
> 🧩 You do **not** need to install Java or PostgreSQL locally.

### 🪟 Windows Setup Tips

1. **Install Maven**  
   Download the [Apache Maven ZIP archive](https://maven.apache.org/download.cgi), extract it, and
   add the `bin` folder
   to your **System PATH**.

2. **Install Docker Desktop**  
   Download it
   from [https://www.docker.com/products/docker-desktop/](https://www.docker.com/products/docker-desktop/).
   </br> Docker compose is included in Docker desktop.

3. ✅ After installation, **restart CMD/PowerShell and your IDE** (e.g., IntelliJ) to apply PATH
   changes.

### 🐧 Linux / macOS

Use your package manager to install Docker and Maven:

```bash
  # Install Maven
  sudo apt update && sudo apt install maven
   
  # Install Docker and Docker Compose plugin
  sudo apt install docker.io docker-compose-plugin
    
  # Start Docker daemon
  sudo systemctl start docker
    
  # Optional: enable Docker at startup
  sudo systemctl enable docker
```

### 🐳 Docker Troubleshooting (Linux / WSL)

> If you encounter issues with `docker compose` not working or returning permission errors, follow
> these steps.

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
Docker Compose version v2.x
```

---

## 📚 Key Dependencies & Tools

This project leverages the following libraries and tooling:

### 📦 Libraries

- **Lombok** — reduces boilerplate for getters/setters/builders via annotations
- **MapStruct** — compile‑time mappings between DTOs and Entities
- **Apache Tika** — file type detection and content inspection for uploads

### 🧰 Developer & CI Tools

- **Checkstyle Plugin** —
  enforces [Google Java Style Guide](https://checkstyle.sourceforge.io/google_style.html)
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

> **Prerequisite:** Make sure Docker is installed and running before starting the application.

### 🐧 On Linux/macOS/WSL

Run the following in your terminal:

```bash
  ./scripts/start.sh
```

🔒 You may need to make the script executable first:

```bash
  chmod +x scripts/start.sh
```

### 🪟 On Windows (CMD or PowerShell)

> Run the following in your terminal:

```cmd
  scripts\start.bat
```

### Or run it manually

```bash
  mvn clean package -DskipTests
  docker compose up --build
```

___

## 🛑 Stopping the Application

### 🐧 Linux/macOS/WSL

```bash
  ./scripts/stop.sh
```

🔒 You may need to make the script executable first:

```bash
  chmod +x scripts/stop.sh
```

### 🪟 On Windows (CMD or PowerShell)

Run the following in your terminal:

```cmd
  scripts\stop.bat
```

If the container got stoped with the `stop script`, this command start it again without executing
the build:

```bash
  docker compose up
```

### 🔁 Full Reset (Wipe Database)

🐧 On Linux/macOS/WSL

```bash
  ./scripts/stop-clean.sh
```

🔒 You may need to make the script executable first on Linux:

```bash
  chmod +x scripts/stop-clean.sh
```

🪟 On Windows (CMD or PowerShell)

```cmd
  scripts\stop-clean.bat
```

---

## 🧬 Database Seeding

The application include two classes for the creation of entities into the database.
One of them allow to create an Admin via the `AdminRunner` class that can be found here
`org/csc/chessclub/config`.
This behavior is manage by the boolean flag `fake.admin.create.flag` which can be found into the
`application-local.properties` file with some other Admin/User properties that allow to change
`Username, Email and Password`.
With the admin is then possible to call every method of the application.

Other than that, there is another class called `DatabaseSeeder` which have the task to create other
entities and
populates the database with some data of `Event, Tournament and Games`.
As for the Admin, this behavior can be enabled or disabled with the property `init.database` located
in the same
application properties file.

🔒 Both of them are enabled by default, so the database will be initialized at the first start or
***only*** if the
volume is empty.

---

## 📘 API Documentation

This section provides a visual overview of the API endpoints for each major entity, followed by a
link to the detailed
Swagger UI.

### Role-Based API Flowchart

This flowchart visualizes the entire API, organized by user role. Each subgraph represents a
distinct user type and the
operations they are permitted to perform.

```mermaid
graph TD
    subgraph UNAUTHENTICATED_USER
        direction LR
        style UNAUTHENTICATED_USER fill:#f9f,stroke:#333,stroke-width:2px
        U_A[GET_events] --> U_B[Get all events];
        U_A2[GET_events_uuid] --> U_B;
        U_C[GET_tournaments] --> U_D[Get all tournaments];
        U_C2[GET_tournaments_uuid] --> U_D;
        U_E[GET_games] --> U_F[Get all games];
        U_E2[GET_games_uuid] --> U_F;
        U_G[GET_games_player_player-name] --> U_F;
        U_H[GET_games_tournamentUuid] --> U_F;
    end

    subgraph AUTHENTICATED_USER
        direction LR
        style AUTHENTICATED_USER fill:#ccf,stroke:#333,stroke-width:2px
        subgraph USER_ROLE
            direction TB
            style USER_ROLE fill:#fff,stroke:#000,stroke-width:1px,stroke-dasharray: 5, 5
            P_U_C[POST_users_login] --> P_U_D[User login];
            P_U_E[GET_users_uuid] --> P_U_F[Get own profile];
            P_U_G[PATCH_users] --> P_U_H[Update own profile];
            P_U_K[POST_events] --> P_U_L[Create new event];
            P_U_M[POST_events_with_PDF] --> P_U_L;
            P_U_N[PATCH_events] --> P_U_O[Update event];
            P_U_P[DELETE_events_uuid] --> P_U_Q[Delete event];
            P_U_R[POST_tournaments] --> P_U_S[Create new tournament];
            P_U_T[PATCH_tournaments] --> P_U_V[Update tournament];
            P_U_W[DELETE_tournaments_uuid] --> P_U_X[Delete tournament];
            P_U_Y[POST_games] --> P_U_Z[Create new game];
            P_U_AA[PATCH_games] --> P_U_AB[Update game];
            P_U_AC[DELETE_games_uuid] --> P_U_AD[Delete game];
        end
    end

    subgraph AUTHENTICATED_ADMIN
        direction LR
        style AUTHENTICATED_ADMIN fill:#cfc,stroke:#333,stroke-width:2px
        subgraph ADMIN_ROLE
            direction TB
            style ADMIN_ROLE fill:#fff,stroke:#000,stroke-width:1px,stroke-dasharray: 5, 5
            A_A[GET_users] --> A_B[Get all users];
            A_C[GET_users_uuid] --> A_D[Get user by ID];
            A_E[PATCH_users] --> A_F[Update any user];
            A_G[PATCH_users_role] --> A_H[Change user role];
            A_I[PATCH_users_password] --> A_J[Change any user password];
            A_K[DELETE_users_uuid] --> A_L[Delete any user];
            A_M[POST/PATCH/DELETE_events] --> A_N[Full Event Access];
            A_M2[GET_events] --> A_N;
            A_O[POST/PATCH/DELETE_tournaments] --> A_P[Full Tournament Access];
            A_O2[GET_tournaments] --> A_P;
            A_Q[POST/PATCH/DELETE_games] --> A_R[Full Game Access];
            A_Q2[GET_games] --> A_R;
        end
    end

    UNAUTHENTICATED_USER --> |Login| AUTHENTICATED_USER;
    AUTHENTICATED_USER --> |Promote| AUTHENTICATED_ADMIN;
   ```

### Swagger UI:

🔗 http://localhost:8080/api/v1/swagger-ui.html

Generate OpenAPI JSON (app must be running)

```bash
  mvn springdoc-openapi:generate
```

> Output file: `target/openapi.json`

---

## 🧪 Testing & Coverage

Run Tests & Generate Coverage Report

```bash
  mvn clean verify
```

> JaCoCo HTML report: `target/site/jacoco/index.html`
---

## 📝 Code Style & Formatting

This project enforces the [Google Java Style Guide](https://checkstyle.sourceforge.io/google_style.html)  
via **Checkstyle** (verification) and **Spotless** (automatic formatting).  
This ensures the same style is applied regardless of IDE.

### Format your code before committing

```bash
  mvn spotless:apply
```

### Verify formatting & style rules

```bash
  mvn clean validate
```

---

## 📄 License

This project is licensed under the MIT License.
---

## 👤 Author

**Fabio Floris**  
💻 [GitHub](https://github.com/FabioFlo/ChessClub)
💼 [LinkedIn](https://www.linkedin.com/in/fabiofloris92)
---
