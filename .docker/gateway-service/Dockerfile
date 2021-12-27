FROM openjdk:11.0.11-jre-slim-buster
RUN apt update && apt install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY ./gateway-service/build/libs/gateway-service-1.0-SNAPSHOT.jar .
CMD ["java", "-jar", "gateway-service-1.0-SNAPSHOT.jar"]