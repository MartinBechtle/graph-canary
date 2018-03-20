FROM openjdk:8-jre-alpine
MAINTAINER Martin Bechtle <martin.bechtle.86@gmail.com>

WORKDIR /app
RUN pwd
RUN ls -la /

ADD bin/run.sh /app
ADD build/libs/graph-canary*.jar /app

EXPOSE 8080
CMD ["/app/run.sh"]
