FROM eclipse-temurin:21
MAINTAINER Philip Mercan
COPY /target/*.jar mls-gateway.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/mls-gateway.jar"]