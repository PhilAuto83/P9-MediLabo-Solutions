FROM eclipse-temurin:21
MAINTAINER Philip Mercan
RUN mvn -f ./pom.xml package -B
COPY /target/*.jar mls-front.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/mls-front.jar"]