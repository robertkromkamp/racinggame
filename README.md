 # racinggame
Racinggame project, which is a Maven Project.

Model is described in RacingGameDomainModel
The model based on JPA annatotions. Ebean is used in the unit tests for the object relation mapping (ORM) and persistence framework. 
Although ebean uses a different approach than JaS (no EntityManager is needed), it uses JPA annotations.
Ebean has a session less architecture. The unit tests are using a H2 in memory database.

Maven:install
Be sure ebean-agent-8.2.1.jar is referenced in the -javaagent property in VM arguments
in my case: -javaagent:C:\Users\rkromkamp\.m2\repository\org\avaje\ebean\ebean-agent\8.2.1\ebean-agent-8.2.1.jar



Server is described in RacingGameServer
The RacingGame server is a webapplication with backend support by Spring MVC and a web-based front end using Thymeleaf template engine 
and jQuery. Spring Boot is used for the webapplication development.

The server has a dependency on RacingGameDomainModel.
It uses the ORM based Ebean persistancy framework and stores the data in a H2 in memory database.

Maven:spring-boot:run
Be sure ebean-agent-8.2.1.jar is referenced in the -javaagent property in VM arguments


