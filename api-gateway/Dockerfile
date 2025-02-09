FROM gradle:7.6-jdk17-alpine AS builder

WORKDIR /build

COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle dependencies

COPY src ./src
RUN gradle build -x test

FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /build/build/libs/my-microservice-*.jar app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "app.jar"]
