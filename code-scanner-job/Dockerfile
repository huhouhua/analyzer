FROM 172.17.162.231/library/alpine-java:1.8.0_221-b11
WORKDIR /app
COPY  target/code-scanner-job.jar  scanner-job.jar
ENTRYPOINT ["java", "-jar", "scanner-job.jar"]