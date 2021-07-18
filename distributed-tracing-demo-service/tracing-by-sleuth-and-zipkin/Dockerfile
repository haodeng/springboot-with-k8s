FROM openjdk:11
COPY target/tracing-by-sleuth-and-zipkin-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT exec java -jar /app.jar