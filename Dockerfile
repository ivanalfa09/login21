# Imagen base con Java 21
#FROM eclipse-temurin:21-jdk-alpine
FROM amazoncorretto:21-alpine

# Directorio de trabajo
WORKDIR /app

# Copiamos el jar generado por Maven
COPY target/*.jar app.jar

# Puerto que expone Spring Boot
EXPOSE 8080

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
