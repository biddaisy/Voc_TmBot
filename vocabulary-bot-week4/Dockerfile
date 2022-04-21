FROM maven:3-openjdk-11 as build-stage
WORKDIR /app
COPY ./ /app/
RUN mvn package

FROM openjdk:11-jre
COPY --from=build-stage /app/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]