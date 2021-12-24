FROM openjdk:11.0.11-jre-slim-buster
RUN apt update && apt install -y curl && rm -rf /var/lib/apt/lists/*
WORKDIR /app
COPY ./learning-center/build/libs/gs-consuming-web-service-0.0.1.jar .
CMD ["java", "-jar", "gs-consuming-web-service-0.0.1.jar"]