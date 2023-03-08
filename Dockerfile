FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar myapp.jar
COPY build/libs/*.conf myapp.conf
COPY pinpoint-bootstrap-2.5.0.jar pinpoint-bootstrap-2.5.0.jar

EXPOSE 8000


ENTRYPOINT ["java","-jar","myapp.jar"]

