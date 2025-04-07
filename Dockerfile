FROM maven:3.8.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY ./VECKOS_Backend /app

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]