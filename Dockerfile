# Multi-stage build for Backend + Frontend in single container
FROM node:18-alpine AS frontend-build

# Set working directory for frontend
WORKDIR /app/frontend

# Copy package files
COPY frontend/package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy frontend source
COPY frontend/ ./

# Build the frontend
RUN npm run build

# Backend build stage
FROM maven:3.9.6-eclipse-temurin-17 AS backend-build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Copy frontend build to Spring Boot static resources
COPY --from=frontend-build /app/frontend/build ./src/main/resources/static/

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

# Install curl for health checks
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Create non-root user for security
RUN groupadd -r movievibes && useradd -r -g movievibes movievibes

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=backend-build /app/target/movievibes-*.jar app.jar

# Change ownership to non-root user
RUN chown movievibes:movievibes app.jar

# Switch to non-root user
USER movievibes

# Expose the port Spring Boot runs on
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
