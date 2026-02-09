# ---------- Build stage ----------
FROM eclipse-temurin:17-jdk-jammy AS builder

WORKDIR /app

# copy project
COPY . .

# use gradle wrapper (NOT gradle)
RUN chmod +x gradlew
RUN ./gradlew clean bootJar -x test


# ---------- Run stage ----------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]
