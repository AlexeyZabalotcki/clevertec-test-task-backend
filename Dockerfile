FROM openjdk:17-alpine
WORKDIR /app
COPY build/libs/clevertec-test-task-backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
