FROM gradle:jdk17-jammy as build
WORKDIR /app
COPY --chown=gradle:gradle . /app
RUN ls -la /app
RUN gradle clean build --no-daemon

FROM openjdk:17.0.2-slim as builder
WORKDIR /app
COPY --from=build /app/build/libs/crypto-exchange-service-1.0.0.jar /app/exchange-service.jar
RUN java -Djarmode=layertools -jar exchange-service.jar extract

FROM openjdk:17.0.2-slim
WORKDIR /app
COPY --from=builder app/dependencies/ ./
COPY --from=builder app/spring-boot-loader/ ./
COPY --from=builder app/snapshot-dependencies/ ./
COPY --from=builder app/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]