FROM openjdk:11
COPY target/secret-demo-service-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -jar /app.jar