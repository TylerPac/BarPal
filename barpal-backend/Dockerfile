# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper and pom.xml files

COPY mvnw* pom.xml .
COPY .mvn .mvn

# Make Maven wrapper executable
RUN chmod +x mvnw

# Copy the source code
COPY src src

# Package the application
RUN ./mvnw clean package -DskipTests

# Expose the port the app runs on
EXPOSE 8080

# Run the Spring Boot application
CMD ["java", "-jar", "target/barpal-0.0.1-SNAPSHOT.jar"]
