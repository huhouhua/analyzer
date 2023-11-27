FROM 172.17.162.231/library/alpine-java:1.8.0_221-b11

WORKDIR /app

COPY  target/code-scanner-scheduler.jar  scanner-scheduler.jar

COPY target/classes/application.yaml application.yaml

ENTRYPOINT ["java", "-jar", "scanner-scheduler.jar","--spring.config.location=./application.yaml"]