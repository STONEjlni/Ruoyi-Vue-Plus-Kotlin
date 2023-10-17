#FROM findepi/graalvm:java17-native
FROM openjdk:17.0.2-oraclelinux8

MAINTAINER Lion Li

RUN mkdir -p /opt/powerjob/logs

WORKDIR /opt/powerjob

ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

EXPOSE 7700

ADD ./target/powerjob-server.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
