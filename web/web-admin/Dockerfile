FROM openjdk:21-jdk-slim

# Install fonts for captcha generation
RUN apt-get update && apt-get install -y fontconfig

WORKDIR /app
ARG JAR_FILE=target/web-admin-1.0-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]