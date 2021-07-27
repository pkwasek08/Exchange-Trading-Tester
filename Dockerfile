FROM maven:3.6.1-jdk-8-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -f pom.xml clean
RUN mvn -f pom.xml install package

FROM openjdk:latest
COPY --from=build /workspace/target/Exchange-Trading-Tester.jar Exchange-Trading-Tester.jar
USER 1001
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "Exchange-Trading-Tester.jar"]