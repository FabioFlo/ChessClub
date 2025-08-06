@echo off
echo 🔨 Building project with Maven...
call mvn clean package -DskipTests
IF %ERRORLEVEL% NEQ 0 (
    echo ❌ Maven build failed!
    exit /b %ERRORLEVEL%
)

echo 🐳 Starting Docker containers...
docker compose up --build
