# Spring Blog Service

## Overview
This project is a Spring Boot-based RESTful API for a blogging service, designed to demonstrate a multi-tier category system with up to 5 levels of depth. 

## Features
- Multi-level category system (up to 5 levels deep)
- RESTful API endpoints for blog operations
- Integration with React-based frontend projects
- Secured endpoints with Spring Security and OAuth2
- Data persistence using JPA and MySQL
- Caching with Spring Cache and Redis
- API documentation with OpenAPI

## Technologies
This project is built with:
- Spring Boot (Data JPA, Web, Security, Cache, Data Redis, OAuth2 Client, Validation)
- Spring Session Data Redis
- Java JWT by Auth0

## Getting Started

### Prerequisites
- Docker
- JDK 17
- Gradle

### Environment Configuration
Create an `env.list` file in the project root with the necessary environment variables:
- API keys
- Database addresses

`application.yml` should be configured with specifics such as `cors.allowed-origins`.

### Building the Docker Image
```shell
docker build -t spring_blog ./
docker run -d --network mynetwork --name spring_blog --env-file ./env.list -p 8080:8080 spring_blog
```


