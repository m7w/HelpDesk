# syntax=docker/dockerfile:1

FROM amd64/eclipse-temurin:21-jdk AS builder
WORKDIR /app
COPY .mvn ./.mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw package

FROM amd64/eclipse-temurin:21-jre
RUN useradd -ms /bin/bash app
USER app
WORKDIR /home/app
COPY --from=builder /app/helpdesk.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "helpdesk.jar"]
