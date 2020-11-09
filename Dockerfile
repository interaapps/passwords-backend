FROM maven:3.6.0-jdk-8-slim AS build

WORKDIR /

COPY src /home/app/src
COPY pom.xml /home/app
# COPY .env .env
# RUN cat .env
RUN mvn -f /home/app/pom.xml clean package

FROM openjdk:8-jre-slim
COPY --from=build /home/app/target/backend.jar /usr/local/lib/backend.jar
# COPY .env .env
# RUN cat .env
EXPOSE 80
ENTRYPOINT ["java","-jar","/usr/local/lib/backend.jar"]