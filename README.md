# Camunda Workflow

Description: Project for test Java Springboot and Camunda stack.

<hr>

## Swagger API documentation:
```
http://localhost:8080/api-docs
```
```
http://localhost:8080/swagger-ui/index.html
```

## Dependency

The project use:

- JDK 21
- Spring Boot 3.2.3.RELEASE
    - DevTools
    - JPA
    - Web
    - Lombok
- Maven
- H2 Database
- OpenAPI

## Observability
````
http://localhost:8080/actuator/prometheus
````

## H2 Database Console
```
http://localhost:8080/h2-console
```

## Endpoints
````
[GET] http://localhost:8080/awards/api/v1/movies/winner
````

## Toogle Feature
````
SENDGRID_API_KEY: To communicate with SendGrid API
SEND_EMAIL: To enable if send email or not (only 100 times per month - free)
EMAIL_SENDER: Email sender configured in SendGrid
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
                └── texoit
                    └── goldenraspberryawards
                        ├── controller
                        ├── model
                        ├── repository
                        ├── service
                        └── GoldenRaspberryAwardsApplication.java
    └── test
..
```

