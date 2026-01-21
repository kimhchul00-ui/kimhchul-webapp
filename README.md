# kimhchul-webapp

Spring Boot 3.3 기반 백오피스 CRUD 애플리케이션

## 기술 스택

- Java 21
- Spring Boot 3.3.0
- Maven
- Spring Data JPA
- H2 Database
- Thymeleaf
- Lombok

## 프로젝트 구조

```
src/
├── main/
│   ├── java/com/kimhchul/webapp/
│   │   ├── entity/          # 엔티티 클래스
│   │   ├── repository/      # JPA Repository
│   │   ├── service/         # 비즈니스 로직
│   │   ├── controller/      # 컨트롤러
│   │   └── KimhchulWebappApplication.java
│   └── resources/
│       ├── application.yml          # 기본 설정
│       ├── application-local.yml    # 로컬 환경 설정
│       ├── application-prod.yml     # 운영 환경 설정
│       ├── data.sql                 # 샘플 데이터
│       └── templates/               # Thymeleaf 템플릿
└── test/
```

## 데이터베이스 스키마

### ORD (주문)
- id: 주문 ID (PK)
- order_no: 주문번호
- customer_name: 고객명
- customer_email: 고객 이메일
- order_date: 주문일시
- total_amount: 총 금액
- status: 주문 상태 (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
- shipping_address: 배송주소

### ORD_ITEM (주문 상품)
- id: 주문 상품 ID (PK)
- ord_id: 주문 ID (FK)
- product_name: 상품명
- product_code: 상품코드
- quantity: 수량
- unit_price: 단가
- total_price: 합계

## 실행 방법

### 로컬 환경
```bash
mvn spring-boot:run
```

또는 프로파일 지정:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### 운영 환경
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## 접속 정보

- 애플리케이션: http://localhost:8080
- H2 콘솔: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb` (로컬)
  - Username: `sa`
  - Password: (비어있음)

## 주요 기능

- 주문 목록 조회 (필터링, 검색)
- 주문 상세 조회
- 주문 등록
- 주문 수정
- 주문 삭제

## 샘플 데이터

애플리케이션 시작 시 `data.sql` 파일을 통해 샘플 주문 데이터가 자동으로 생성됩니다.
