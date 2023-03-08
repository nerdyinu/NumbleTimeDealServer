FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar myapp.jar
COPY build/libs/*.conf myapp.conf

EXPOSE 8000


ENTRYPOINT ["java","-jar","myapp.jar"]

