FROM 172.17.162.231/library/alpine-java:1.8.0_221-b11

WORKDIR /app

COPY  target/code-scanner-job.jar  scanner-job.jar

COPY target/classes/application-prod.yaml application-prod.yaml

COPY private_key /etc/git/private_key

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["java", "-jar", "scanner-job.jar","--spring.config.location=./application-prod.yaml"]