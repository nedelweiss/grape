FROM openjdk:11-jre

RUN mkdir /app
COPY build/libs/spd-google-0.0.1-SNAPSHOT.war /app

ENTRYPOINT ["java", "-jar", "/app/spd-google-0.0.1-SNAPSHOT.war"]