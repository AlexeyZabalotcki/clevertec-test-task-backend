FROM openjdk:17
WORKDIR /app
COPY build/libs/clevertec-test-task-backend-0.0.1-SNAPSHOT.jar backend-0.0.1-SNAPSHOT.jar
COPY src/main/resources/template.png /app/resources/
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT.jar"]
