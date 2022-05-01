# This is Sales Service for the interview
### Question 1: propose a protocol / method and justify your choice.
**OAuth2 (JWT)** and **User-Role-Permission** based security would be my choice for this Sales application and similar REST APIs.<br>
In microservices environment I would create users microservice with responsibility to authenticate a user and issue a JWT.<br>
This sales-service would be a resource server (in Oauth 2.0).

**Justification:** This set-up would enable the company to use Single-sign-on services. For example: AWS Cognito or OKTA.<br>
Also this would give the company a single place to maintain users and configure roles: For example: onboard new colleagues.

This sales service would have 2 different roles: 
- BUYER
- SELLER

Permissions would be:
- READ_PRODUCT
- EDIT_PRODUCT (includes create and update)
- DELETE_PRODUCT
- READ_ORDER
- EDIT_ORDER (includes create and update)

Proposed Auth code implementation: 
1. Spring Security, spring-security-oauth2, custom UserDetails implementation with User information and permissions
2. Url path matchers in Spring Security Configuration
3. Depending on use case, @PreAuthorize annotation. If data and access definition is dynamic (defined by other users) - spring-security-acl

_Note: In non interview scenario, I would gather more details on the problem we are solving :) Business requirements could influence IT decisions._

### Question 2: How can you make the service redundant? What considerations should you do?
1. Service registry and auto discovery should be enabled if we are moving to microservices: Netflix Eureka 
2. For the front-end we need API Gateway: spring-cloud-gateway
3. Spring cloud config server to manage service configs.
4. Distributed systems logging, analytics and monitoring should be considered. Autoscaling distributed systems makes this complex.
For example Grafana, Prometheus, DataDog or AppDynamics. Log aggregation, services health monitoring, correlationId are necessary.<br>
5. Service scaling autoconfiguration should be considered. Do we scale on server metrics, some custom metrics, some schedule?<br>
Scaling policies configuration is defined in Kubernetes configuration.

# Project development environment setup
## Start MariaDB database on docker
1. Open terminal and navigate to project sources. Execute `cd docker`
2. `docker-compose up`
3. Wait for text `[Note] mariadbd: ready for connections.`

Stop / Destroy the container `docker-compose down` `docker-compose rm`

## Start Sales Service App
1. Project requires JDK 17
2. Execute `gradlew build` to build source code
3. Execute `gradlew bootRun` to start the project

## REST API documentation - Swagger
[Swagger-ui](http://localhost:8080/swagger-ui) documentation is configured. Documentation is sourced <br>
from RestController JavaDocs by using springdoc **therapi-runtime-javadoc-scribe** Comments with @ApiOperation are also supported