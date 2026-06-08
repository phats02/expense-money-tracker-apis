# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src src

# Build the application (skip tests for faster build)
RUN mvn package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "app.jar"]
