# Use Temurin Java 17 JDK as the base
FROM eclipse-temurin:17-jdk

LABEL maintainer="dilipgona.dev@gmail.com"

# Copy the built Spring Boot JAR
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Order-Service listens on 8083
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app.jar"]