FROM eclipse-temurin:17

WORKDIR /app/backend

COPY . .

RUN chmod +x gradlew
RUN ./gradlew clean build -x test

CMD ["java","-jar","build/libs/*.jar"]
