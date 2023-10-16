# Use OpenJDK 8 as the base image
FROM openjdk:17-oracle

# Set the working directory in the container
WORKDIR /deploy

RUN mkdir -p /deploy/cmd/zk-slack

COPY build/libs/zk-slack-0.0.1-SNAPSHOT.jar /deploy/zk-slack.jar

EXPOSE 80

# Set the entry point to run the JAR file with the application.yaml file
ENTRYPOINT ["java", "-jar", "zk-slack.jar", "--spring.config.location=file:/opt/zk-slack-configmap.yaml"]


