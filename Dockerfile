FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar myapp.jar
COPY pinpoint-bootstrap-2.5.0.jar pinpoint-bootstrap-2.5.0.jar

EXPOSE 8000


ENTRYPOINT ["java","-jar", "-javaagent:/app/pinpoint-bootstrap-2.5.0.jar ", "  -Dpinpoint.agentId=app-1", "-Dpinpoint.applicationName=app", "myapp.jar"]

