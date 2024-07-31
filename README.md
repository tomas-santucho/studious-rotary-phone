
# Ecommerce Challenge

This repository contains an application for managing orders, products, and order items. The application is built using Spring Boot, ModelMapper, and other modern Java libraries. It includes comprehensive APIs for managing orders, products, and order items, along with validation and exception handling.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Building the Application](#building-the-application)
- [Running the Application](#running-the-application)
- [Testing the Application](#testing-the-application)
- [API Documentation](#api-documentation)

## Prerequisites

Before you start, make sure you have the following software installed:

- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)
- [Java 21](https://jdk.java.net/21/)
- [Gradle](https://gradle.org/install/)

## Building the Application

To build the application, follow these steps:

1. Clone the repository:
   ```sh
   git clone https://github.com/tomas-santucho/studious-rotary-phone.git
   cd ecommerce-challenge
   ```

2. Build the application using Gradle:
   ```sh
   ./gradlew build
   ```

## Running the Application

To run the application, follow these steps:

1. Ensure Docker is running on your machine.

2. Use Docker Compose to start the application:
   ```sh
   docker-compose up --build
   ```

3. The application will be available at `http://localhost:8080`.

## Testing the Application

To run the tests, follow these steps:

1. Ensure Docker is running on your machine.

2. Use Docker Compose to run the tests:
   ```sh
   docker-compose run app ./gradlew test
   ```

## API Documentation

The application includes comprehensive API documentation using OpenAPI and Swagger. Once the application is running, you can access the API documentation at:

```
http://localhost:8080/swagger-ui/index.html
```

## Exception Handling

The application includes global exception handling for validation errors and resource not found exceptions. This ensures that API responses are consistent and provide meaningful error messages.
