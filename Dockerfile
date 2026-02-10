# ---------- STAGE 1 : BUILD ----------
FROM eclipse-temurin:17-jdk AS builder

WORKDIR /app

# copy ONLY backend
COPY backend/ ./backend/

WORKDIR /app/backend

# use project wrapper (VERY IMPORTANT)
RUN chmod +x gradlew
RUN ./gradlew clean build -x test


# ---------- STAGE 2 : RUN ----------
FROM eclipse-temurin:17-jre

WORKDIR /app

# copy built jar
COPY --from=builder /app/backend/build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
