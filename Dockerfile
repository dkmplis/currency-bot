FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline-plugins || true
RUN mvn dependency:resolve-plugins dependency:resolve -B
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
WORKDIR /app
COPY --from=builder /app/target/currency-bot.jar currency-bot.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "currency-bot.jar"]
