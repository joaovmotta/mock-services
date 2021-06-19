FROM openjdk:8-jre-alpine
MAINTAINER "Mock Services"
RUN mkdir -p /opt/web/mock-services
COPY build/libs/mock-service-0.0.1-SNAPSHOT.jar /opt/web/mock-services

WORKDIR /opt/web/mock-services
EXPOSE 8080
CMD ["sh","-c","java -jar mock-service-0.0.1-SNAPSHOT.jar"]