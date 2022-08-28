# syntax=docker/dockerfile:1

FROM amazoncorretto:17-alpine

WORKDIR /app

COPY .mvn ./.mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN ./mvnw -DskipTests=true package

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "helpdesk.jar"]
