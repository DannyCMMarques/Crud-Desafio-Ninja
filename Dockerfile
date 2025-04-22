# Etapa 1: Construção da Aplicação com Maven e JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia o arquivo pom.xml para resolver dependências
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código fonte
COPY src ./src
COPY src/main/resources /app/resources/

# Compila o projeto e gera o JAR
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final com JDK 17
FROM openjdk:17-jdk-slim
WORKDIR /app

# Copia o JAR compilado da etapa de construção
COPY --from=build /app/target/*.jar .

# Executa a aplicação
ENTRYPOINT ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
