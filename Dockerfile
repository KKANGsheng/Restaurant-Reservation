#Pick base image

ARG BASE_JDK_IMAGE=eclipse-temurin:21-jdk-alpine
ARG BASE_JRE_IMAGE=eclipse-temurin:21-jre-alpine

# Stage 1: Cache Maven dependencies
## Maven needs JDK
FROM ${BASE_JDK_IMAGE} As dependencies

## Install Maven via Alpine package manager
RUN apk add --no-cache maven

## Create + cd into /build.
WORKDIR /build

## Copy pom
COPY pom.xml .

## Download all deps to local maven
RUN mvn dependency:go-offline -B

# Stage 2:Build the jar
FROM dependencies As builder

## Copy source code
COPY src ./src

## Compile + package
RUN mvn clean package -DskipTests -B

# Stage 3:Runtime (JRE ONly)
FROM ${BASE_JRE_IMAGE} As runtime
WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --from=builder /build/target/*/jar app.jar

LABEL org.opencontainers.image.authors="kang Sheng" \
      org.opencontainers.image.version="1.0" \
      org.opencontainers.image.title="Merchant Based Reservation app"

USER app

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s \
    CMD wget -q0- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT["Java","-XX:MaxRAMPercentage=75.0","-jar","app.jar"]