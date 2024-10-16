# Paso 1: Usar una imagen base que tenga Java 17 instalado
FROM openjdk:17-jdk-alpine

# Paso 2: Instalar Maven
RUN apk add --no-cache maven

# Paso 3: Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Paso 4: Copiar el código fuente de la aplicación a la imagen Docker
COPY . /app

# Paso 5: Ejecutar el comando Maven para compilar y empaquetar la aplicación
RUN mvn clean package

# Paso 6: Exponer el puerto que utiliza la aplicación (por defecto 8080)
EXPOSE 8080

# Paso 7: Ejecutar la aplicación usando el JAR generado por Maven
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]