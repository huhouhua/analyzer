FROM 172.17.162.231/library/alpine-java:1.8.0_221-b11

WORKDIR /app

COPY  target/analyzer-scheduler.jar  analyzer-scheduler.jar

COPY target/classes/application-prod.yaml application-prod.yaml

COPY private_key /etc/git/private_key

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "analyzer-scheduler.jar","--spring.config.location=./application-prod.yaml"]