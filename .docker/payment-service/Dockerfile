FROM openjdk:11.0.11-jre-slim-buster
RUN apt update && apt install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY ./payment-service/build/libs/gs-producing-web-service-0.0.1.jar .
CMD ["java", "-jar", "gs-producing-web-service-0.0.1.jar"]