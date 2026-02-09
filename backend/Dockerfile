# Use lightweight Java 17 image
FROM eclipse-temurin:17-jdk-jammy

# App directory
WORKDIR /app

# Copy built jar
COPY build/libs/*.jar app.jar

# Expose Spring port
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","app.jar"]
