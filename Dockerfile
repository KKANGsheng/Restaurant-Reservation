FROM amazoncorretto:21-alpine-jdk

WORKDIR /app

ARG JAR_FILE=target/*.jar

COPY  ${JAR_FILE} app.jar

LABEL org.opencontainers.image.authors="kang Sheng" \
    org.opencontainers.image.version="1.0" \
    org.opencontainers.image.title="Merchant Based Reservation app"

EXPOSE 8080
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]