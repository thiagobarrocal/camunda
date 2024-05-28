# Camunda Workflow

Description: Project for test Java Springboot and Camunda stack.

<hr>

## Future tec improvements
````
1. Async process to send email (RabbitMQ)
2. Add more tests
````

## Swagger API documentation:
```
http://localhost:8080/api-docs
```
```
http://localhost:8080/swagger-ui/index.html
```

## Dependency

The project use:

- JDK 17
- Spring Boot 3.2.2.RELEASE
    - Camunda v7
    - DevTools
    - JPA
    - Web
    - Lombok
    - MapStruct
    - Actuator
    - SendGrid
- Maven
- H2 Database
- OpenAPI

## Observability
````
http://localhost:8080/actuator/prometheus
````

## Requests trace observability
````
Every request controller
'@LogAround' annotation
trace example ->  
A execucao do metodo [ResponseEntity create(QuoteRequestDto)] durou [PT0.001029333S]
````

## H2 Database Console
```
http://localhost:8080/h2-console
Jdbc url: jdbc:h2:mem:camunda-db
Username: sa
Password: sa
```

## Endpoints
````
## Process ##
[POST] http://localhost:8080/v1/process/start
[POST] http://localhost:8080/v1/process/cancel

## Quote ##
[POST] http://localhost:8080/v1/quote
````

## Toogle Feature
````
SENDGRID_API_KEY: To communicate with SendGrid API
SEND_EMAIL: [default:false] To enable if send email or not (only 100 times per month - free)
EMAIL_SENDER: Email sender configured in SendGrid
````

## BPMN files
````
    └── src
        └── main
            └── resources
                └── processes
                    └── flow.bpmn
                    └── dmn.dmn
                    └── form1.form
                    └── form2.form
````

## How to run the application
```
1. Build the project using [mvn clean install]
2. Run [mvn spring-boot:run]
3. Access the application at [http://localhost:8080/awards/api/v1/movies/winner]
```

## How to test the application
```
mvn spring-boot:test
```

## Structure

Used pattern Package by Layer for general directory structure:

```
├── src
    └── main
        └── java
            └── com
                └── example
                    └── workflow
                        ├── config
                        ├── controller
                        ├── exception
                        ├── gateway
                        ├── model
                        ├── prser
                        ├── repository
                        ├── service
                        ├── steps
                        ├── utils
                        └── Application.java
                      
    └── test
        └── java
            └── com
                └── example
                    └── workflow
                        └── steps                 
..
```

