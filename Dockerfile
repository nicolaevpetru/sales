# Use a base image with Java 11
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Copy the packaged jar file into the container
COPY target/your-app-name.jar app.jar

# Expose the port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]