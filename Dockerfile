# Этап сборки
FROM openjdk:26-ea-25-jdk-slim

RUN mkdir -p /opt

COPY target/cookbook-server-*.jar /opt/app/cookbook.jar

ENTRYPOINT ["java", "-jar", "/opt/app/cookbook.jar"]

EXPOSE 8080