#!/bin/bash

echo "🔨 Building project with Maven..."
mvn clean package -DskipTests || { echo "❌ Maven build failed!"; exit 1; }

echo "🐳 Starting Docker containers..."
docker compose up --build
