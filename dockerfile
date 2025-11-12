# Multi-stage build to optimize image size
FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /app

# Copy Maven descriptor and sources
COPY pom.xml .
COPY src ./src

# Install Maven and build the application
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
RUN mvn clean package -DskipTests

# Final stage
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Install helpful tools for debugging
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Copy the built JAR from the builder stage
COPY --from=builder /app/target/safepasto-backend-1.0.0.jar app.jar

# Create non-root user for security
RUN groupadd -r spring && useradd -r -g spring spring
USER spring

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD curl -f http://localhost:8080/api/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]