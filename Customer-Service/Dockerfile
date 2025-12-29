# Use Temurin Java 17 JDK as the base
FROM eclipse-temurin:17-jdk

# Add metadata (optional)
LABEL maintainer="your-email@example.com"

# Copy the built JAR
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose port your app listens on (e.g., 8081)
EXPOSE 8081

# Run the JAR
ENTRYPOINT ["java", "-jar", "/app.jar"]
