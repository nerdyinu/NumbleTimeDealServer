## 타임딜 서버 챌린지

### ERD
```
+-------------+     +-------+     +---------+
|  Customer   |     | Order |     | Product |
+-------------+     +-------+     +---------+
| PK: id      |-----| PK: id|-----| PK: id  |
| name        |1   *|       |*   1| _name   |
| password    |     |       |     | _description |
| role        |     |       |     | _appointedTime |
| createdDate |     |       |     | _appointedQuantity |
| updatedDate |     |       |     | admin_id (FK) |
|             |     |       |     | _stockQuantity |
|             |     |       |     | versionNo |
|             |     |       |     | createdDate |
|             |     |       |     | updatedDate |
+------+------+     +-------+     +----+----+
       ^                           |
       |                           |
       +---------------------------+
                 admin_id (FK)

```

고객과 주문, 주문과 상품이 각각 일대다, 다대일 관계를 가집니다. 
고객과 상품이 맺는 다대다 관계의 중간 관계 엔티티 역할을 주문이 하게 됩니다.

이뿐만 아니라, 상품은 자신을 등록한 ADMIN 유저에 대한 외래키를 통해 Customer 테이블을 참조합니다. 
이는 단방향 관계로 Customer 테이블에는 표현되지 않습니다.

### API 명세
API 명세는 RestDocs를 사용하여 생성하였습니다.

### 시스템 아키텍쳐

Product, Customer, Order 3개의 엔티티/테이블을 가지며, 컨트롤러/서비스/레포지토리를 각각 구분하여 가지고 있습니다.
가이드라인에 명시된

회원 : 가입/탈퇴/조회 기능

상품 : 등록/수정/삭제/목록/상세 기능

구매 : 구매하기 기능

의 기준으로 이를 구분하려 했으나, 실제로 서비스에서는 레포지토리 구분 없이 여러 레포지토리에 의존성을 가지고 있습니다.

모든 레포지토리는
```kotlin
interface Repository:JPARespotiroy<Entity, UUID>, RepositoryCustom
interface RepositoryCustom
class RepositoryCustomImpl
```
의 형태로 의존성을 설계했습니다.

상품마다 각기 고유한 "판매시간" 필드를 가지고, 또 해당 시간마다 자동으로 수복되는 정해지만 "판매재고" 필드를 가지며 스프링 스케줄의 크론 표현식을 사용해 1분마다 판매시간이 일치하는 상품을 가져와 재고를 업데이트하는 메서드를 갖는 서비스를 등록했습니다.

### 젠킨스 파이프라인
젠킨스 파이프라인은 다음과 같습니다

```sql
+---------------------+
|       Pipeline      |
+---------------------+
            |
            v
+---------------------+
|      Checkout       |
|  git checkout cmd   |
+---------------------+
            |
            v
+---------------------+
|        Build        |
| ./gradlew clean build|
+---------------------+
            |
            v
+---------------------+
|         Test        |
|  ./gradlew test     |
+---------------------+
            |
            v
+---------------------+
|       Package       |
| ./gradlew bootJar   |
+---------------------+
            |
            v
+---------------------+
| Build Docker Image  |
|  docker.build(...)  |
|  docker.image(...)  |
+---------------------+
            |
            v
+---------------------+
| Deploy Docker Cont. |
| docker stop/rm/run  |
+---------------------+
            |
            v
+---------------------+
|    Get Test List    |
|  curl -X GET        |
|  jsonResponse.content.id.join(',')|
+---------------------+
            |
            v
+---------------------+
|      Run Tests      |
|  curl -X PUT        |
|  curl -X GET        |
|  jsonResponse.status|
+---------------------+

```

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
License
This project is licensed under the MIT License - see the LICENSE file for details.


You can customize this README.md to better fit your project's requirements or add any additional information that you think is necessary.



