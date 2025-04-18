# Etapa 1: Construção da Aplicação
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY src/main/resources /app/resources/

RUN mvn clean package -DskipTests

# Etapa 2: Imagem final
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar .

ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
