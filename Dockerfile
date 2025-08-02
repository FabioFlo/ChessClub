FROM eclipse-temurin:21-jdk-jammy as builder

WORKDIR /app

# Copy your jar (make sure it's built first with mvn clean package)
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the default port
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
