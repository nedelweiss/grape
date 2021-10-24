FROM openjdk:11-jre

RUN mkdir /app
COPY build/libs/grape-0.0.1-SNAPSHOT.war /app

ENTRYPOINT ["java", "-jar", "/app/grape-0.0.1-SNAPSHOT.war"]