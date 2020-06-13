FROM adoptopenjdk/openjdk11:jdk-11.0.7_10-alpine
COPY target/demo-0.0.1-SNAPSHOT.jar .
CMD java -jar demo-0.0.1-SNAPSHOT.jar