#!/bin/bash
./gradlew clean
./gradlew :learning-center:bootJar
./gradlew :payment-service:bootJar