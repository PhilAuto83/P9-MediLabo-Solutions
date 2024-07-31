FROM eclipse-temurin:21
MAINTAINER Philip Mercan
COPY /target/*.jar eureka-server.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "/eureka-server.jar"]