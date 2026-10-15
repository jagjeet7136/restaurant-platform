FROM maven:3.10-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=build /app/target/restaurant-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 1000

ENTRYPOINT ["java", "-jar", "app.jar"]