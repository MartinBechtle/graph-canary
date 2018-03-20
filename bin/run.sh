#!/bin/sh

echo "Starting Spring Boot app with profile [${SPRING_PROFILES_ACTIVE}]"

# Find first accounts-api jar file, we expect there to be only one
APP_JAR=$(find . -mindepth 1 ! -type l -name "graph-canary*.jar" | head -n 1)

echo "Using jar [${APP_JAR}]"
echo "Using JAVA_OPTIONS [${JAVA_OPTIONS}]"

java ${JAVA_OPTIONS} -jar "${APP_JAR}"
