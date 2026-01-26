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

### ITEM (상품)
- id: 상품 ID (PK)
- item_code: 상품코드 (고유)
- item_name: 상품명
- description: 상품 설명
- price: 가격
- stock_quantity: 재고 수량
- status: 상태 (ACTIVE, INACTIVE, SOLD_OUT)
- image_url: 이미지 URL
- created_date: 등록일시
- updated_date: 수정일시

### UITEM (상품 옵션)
- id: 옵션 ID (PK)
- item_id: 상품 ID (FK)
- option_name: 옵션명 (예: 색상, 사이즈)
- option_value: 옵션값 (예: 빨강, L)
- additional_price: 추가 가격
- stock_quantity: 재고 수량
- status: 상태 (ACTIVE, INACTIVE, SOLD_OUT)

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
- item_id: 상품 ID (FK, 선택)
- uitem_id: 옵션 ID (FK, 선택)
- product_name: 상품명
- product_code: 상품코드
- quantity: 수량
- unit_price: 단가
- total_price: 합계

### PAYMENT (결제)
- id: 결제 ID (PK)
- ord_id: 주문 ID (FK)
- payment_type: 결제 타입 (CARD, BANK_TRANSFER, MOBILE, CASH)
- payment_method: 결제 수단
- amount: 결제 금액
- payment_status: 결제 상태 (PAYMENT, REFUND, PARTIAL_REFUND)
- payment_date: 결제일시
- refund_date: 환불일시
- refund_reason: 환불 사유
- transaction_id: 거래 ID

### ORDER_FEE (주문 비용)
- id: 비용 ID (PK)
- ord_id: 주문 ID (FK)
- fee_type: 비용 타입 (SHIPPING, PACKAGING, TIP, ETC)
- fee_name: 비용명
- amount: 비용 금액
- description: 비용 설명

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

### 관리자용 (Admin)
- **주문 목록 조회**: 날짜 범위, 상태, 고객명으로 필터링 및 검색
- **페이징**: 페이지당 10/30/50/100개 항목 선택 가능
- **주문 상세 조회**: 주문 정보, 상품 목록, 추가 비용, 결제 정보 확인
- **주문 등록/수정**: 주문 정보, 상품, 추가 비용, 결제 정보 관리
- **주문 삭제**: 주문 삭제 기능
- **접속 경로**: `/orders`
- **상품 목록 조회**: 상태, 상품명/코드로 필터링 및 검색
- **상품 등록/수정**: 상품 정보, 옵션 관리
- **상품 삭제**: 상품 삭제 기능
- **접속 경로**: `/admin/items`

### 고객용 (Customer)
- **주문 내역 조회**: 날짜 범위 검색, 페이징 지원
  - 접속 경로: `/customer/orders`
  - 신세계몰 스타일의 깔끔한 카드 기반 UI
- **주문 상세 조회**: 주문 정보, 상품 목록, 추가 비용, 결제 정보 확인
  - 접속 경로: `/customer/orders/{id}`
- **주문하기**: 주문서 작성 및 제출
  - 접속 경로: `/customer/orders/new`
  - 주문자 정보 입력
  - 상품 추가/삭제 (동적 추가 가능)
  - 추가 비용 입력 (배송비, 포장비, 팁 등)
  - 실시간 주문 요약 계산
  - 신세계몰 스타일의 모던한 UI
- **상품 목록 조회**: 등록된 상품 목록 확인
  - 접속 경로: `/customer/items`
  - 그리드 레이아웃의 상품 카드
- **상품 상세 조회**: 상품 정보, 옵션 선택, 바로구매
  - 접속 경로: `/customer/items/{id}`
  - 옵션 선택 및 수량 조절
  - 바로구매 기능 (주문서로 자동 이동)

## 샘플 데이터

애플리케이션 시작 시 `data.sql` 파일을 통해 샘플 데이터가 자동으로 생성됩니다:
- 상품 데이터: 22개의 샘플 상품
- 옵션 데이터: 각 상품별 옵션 정보
- 주문 데이터: 15개의 샘플 주문
- 주문 상품 데이터: 각 주문별 상품 정보

## UI 특징

### 관리자용 UI
- 백오피스 스타일의 차분한 디자인
- 연한 파스텔 톤의 상태 배지 및 버튼
- 모바일 반응형 지원
- 상세한 필터링 및 검색 기능

### 고객용 UI
- 신세계몰 스타일의 모던한 디자인
- 깔끔한 카드 기반 레이아웃
- 직관적인 주문서 작성 인터페이스
- 실시간 금액 계산 기능
- 모바일 친화적 반응형 디자인
