#!/bin/bash
./gradlew clean
./gradlew :discovery-service:bootJar
./gradlew :gateway-service:bootJar
./gradlew :config-service:bootJar
./gradlew :learning-center:bootJar
./gradlew :payment-service:bootJar
