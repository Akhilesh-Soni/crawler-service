# CRAWLER SERVICE

An​ application which allows users​ to crawl the site by providing the url. 
The user can get the list of sites that was crawled with static contents urls, other domain urls and external urls. 
It exposes a REST interface which accepts HTTP requests from web client like Postman or Advance Rest Client.

How to start the rating service
---

1. Run `mvn clean install` to build your application
2. Start application with `java -jar target/crawler-service-1.0.0-SNAPSHOT.jar server config.yml`
3. To check that your application is running enter url `http://localhost:8080`


How to create docker image and run it(Docker should be installed)
---

1. Inside crawler-service folder open terminal.
2. Run `sudo docker build -t crawler-service:1.0.0-SNAPSHOT .` to build a docker image.
3. Run `sudo docker run --name=crawler-service -p 8080:8080 -p 8081:8081 crawler-service:1.0.0-SNAPSHOT` to run the docker image.


Health Check
---

To see your applications health enter url `http://localhost:8081/healthcheck`
