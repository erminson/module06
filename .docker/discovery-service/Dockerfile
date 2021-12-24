FROM openjdk:11.0.11-jre-slim-buster
RUN apt update && apt install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY ./discovery-service/build/libs/discovery-service-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "discovery-service-1.0-SNAPSHOT.jar"]