# Multi-stage build for optimal image size
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml first for better caching
COPY pom.xml .

# Download dependencies (this layer will be cached if pom.xml doesn't change)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre-jammy

# Create non-root user for security
RUN groupadd -r movievibes && useradd -r -g movievibes movievibes

# Set working directory
WORKDIR /app

# Copy the JAR file from build stage
COPY --from=build /app/target/movievibes-*.jar app.jar

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
