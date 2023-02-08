FROM java:8

ENV SERVER_PORT 8999

ADD  target/study.jar study.jar

ENTRYPOINT ["java", "-jar","/study.jar"]
