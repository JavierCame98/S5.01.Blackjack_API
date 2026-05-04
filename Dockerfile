# ── Etapa 1: Build ────────────────────────────────────────────────────────────
# Usamos la imagen oficial de Maven con JDK 21 para compilar y empaquetar
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiamos pom.xml primero para aprovechar la caché de capas de Docker:
# si el pom no cambia, Maven no vuelve a descargar dependencias en builds posteriores
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiamos el código fuente y generamos el JAR (sin tests: ya los pasamos en CI)
COPY src ./src
RUN mvn package -DskipTests -B

# ── Etapa 2: Runtime ──────────────────────────────────────────────────────────
# Imagen JRE Alpine (~80 MB) — más ligera y más rápida de descargar que la variante normal
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiamos solo el JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto en el que escucha Spring Boot
EXPOSE 8080

# Arrancamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
