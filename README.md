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

