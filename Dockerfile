FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/*.jar myapp.jar
COPY pinpoint-agent-2.5.0.tar.gz pinpoint-agent.tar.gz
RUN tar -zxvf pinpoint-agent.tar.gz
RUN chmod 755 /pinpoint-agent/pinpoint-bootstrap-2.5.0.jar
EXPOSE 8000
ENV JAVA_OPTS="-javaagent:/pinpoint-agent/pinpoint-bootstrap-2.5.0.jar -Dpinpoint.agentId=app-1 -Dpinpoint.applicationName=app"
ENTRYPOINT exec java ${JAVA_OPTS} -jar myapp.jar