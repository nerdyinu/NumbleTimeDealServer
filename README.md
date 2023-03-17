

# Numble Time Deal Server

Numble Time Deal Server is a Kotlin-based Spring Boot project that provides a back-end server for a limited-time deal system. The application allows users to register, login, and manage products and orders.

## Features

- User registration and authentication with role-based access control
- Product management (create, read, update, delete)
- Order management (create, read)
- Daily product stock updates

## Prerequisites

- JDK 11 or higher
- Gradle
- MySQL

## Installation

1. Clone the repository:

git clone https://github.com/inudev5/NumbleTimeDealServer.git

2. Navigate to the project directory:

`cd NumbleTimeDealServer`

3. Create a MySQL database and configure the `application.yml` file with your database connection details:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
    username: your_database_username
    password: your_database_password
```


4. Run the application:

`./gradlew bootRun`

The server will be running on http://localhost:8080.

API Endpoints
/api/v1/register: User registration
/api/v1/login: User login
/api/v1/products: List all products (role-based access)
/api/v1/products/{id}: Get product details
/api/v1/products/{id}/update: Update product details
/api/v1/orders: Create and list orders

API docs created with Spring RESTDOCS:
https://inudev5.github.io/NumbleBankingServerChallenge/


License
This project is licensed under the MIT License - see the LICENSE file for details.


You can customize this README.md to better fit your project's requirements or add any additional information that you think is necessary.

### WIKI

https://github.com/inudev5/NumbleTimeDealServer/wiki/%ED%83%80%EC%9E%84%EB%94%9C-%EC%84%9C%EB%B2%84-%EC%B1%8C%EB%A6%B0%EC%A7%80

