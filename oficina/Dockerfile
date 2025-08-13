# Etapa 1: Build
FROM eclipse-temurin:24-jdk AS builder

WORKDIR /app

# Copia apenas arquivos de build primeiro (cache mais eficiente)
COPY pom.xml mvnw ./
COPY .mvn .mvn

# Dá permissão de execução pro Maven Wrapper
RUN chmod +x mvnw

# Baixa dependências para aproveitar cache
RUN ./mvnw dependency:go-offline -B

# Copia o restante do código
COPY src src

# Build da aplicação
RUN ./mvnw package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:24-jdk

WORKDIR /app

# Copia o jar da etapa de build
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8001

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
