FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar myapp.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","myapp.jar"]
