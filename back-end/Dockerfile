# syntax=docker/dockerfile:1

FROM ibm-semeru-runtimes:open-17-jdk AS builder
WORKDIR /app
COPY .mvn ./.mvn
COPY mvnw pom.xml ./
COPY src ./src
RUN ./mvnw package

FROM ibm-semeru-runtimes:open-17-jre
RUN useradd -ms /bin/bash app
USER app
WORKDIR /home/app
COPY --from=builder /app/helpdesk.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "helpdesk.jar"]
