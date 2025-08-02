# Dockerfile for Spring Boot application
FROM openjdk:18-jdk-slim

# Set working directory
WORKDIR /app

# Copy the pre-built JAR file
COPY target/visuasset.jar app.jar

# Create directory for H2 database files
RUN mkdir -p .db/dev

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]