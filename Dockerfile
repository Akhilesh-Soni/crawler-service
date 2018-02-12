FROM openjdk:8

ADD target/crawler-service-1.0.0-SNAPSHOT.jar /var/lib/service.jar
ADD config.yml /etc/config.yml

EXPOSE 8080 8081

WORKDIR /etc/

#some comment

ENTRYPOINT ["java", "-jar", "/var/lib/service.jar"]
CMD ["server", "config.yml"]

