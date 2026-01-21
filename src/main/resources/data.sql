-- 샘플 주문 데이터 (ID는 자동 생성되므로 제외)
INSERT INTO ORD (ORDER_NO, CUSTOMER_NAME, CUSTOMER_EMAIL, ORDER_DATE, TOTAL_AMOUNT, STATUS, SHIPPING_ADDRESS) VALUES
('ORD-001', '홍길동', 'hong@example.com', TIMESTAMP '2024-01-15 10:30:00', 150000, 'CONFIRMED', '서울시 강남구 테헤란로 123'),
('ORD-002', '김철수', 'kim@example.com', TIMESTAMP '2024-01-16 14:20:00', 89000, 'SHIPPED', '서울시 서초구 서초대로 456'),
('ORD-003', '이영희', 'lee@example.com', TIMESTAMP '2024-01-17 09:15:00', 320000, 'DELIVERED', '서울시 송파구 올림픽로 789'),
('ORD-004', '박민수', 'park@example.com', TIMESTAMP '2024-01-18 16:45:00', 125000, 'PENDING', '서울시 마포구 홍대로 321'),
('ORD-005', '정수진', 'jung@example.com', TIMESTAMP '2024-01-19 11:00:00', 210000, 'CONFIRMED', '서울시 종로구 세종대로 654');

-- 샘플 주문 상품 데이터 (ID는 자동 생성되므로 제외, ORD_ID는 위에서 생성된 주문의 ID를 참조)
INSERT INTO ORD_ITEM (ORD_ID, PRODUCT_NAME, PRODUCT_CODE, QUANTITY, UNIT_PRICE, TOTAL_PRICE) VALUES
(1, '노트북 스탠드', 'PROD-001', 2, 50000, 100000),
(1, '무선 마우스', 'PROD-002', 1, 50000, 50000),
(2, '키보드', 'PROD-003', 1, 89000, 89000),
(3, '모니터 27인치', 'PROD-004', 1, 250000, 250000),
(3, 'HDMI 케이블', 'PROD-005', 2, 35000, 70000),
(4, 'USB 허브', 'PROD-006', 3, 25000, 75000),
(4, '케이블 정리함', 'PROD-007', 1, 50000, 50000),
(5, '웹캠', 'PROD-008', 1, 120000, 120000),
(5, '마이크', 'PROD-009', 1, 90000, 90000);
