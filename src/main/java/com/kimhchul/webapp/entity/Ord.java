package com.kimhchul.webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ORD")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORDER_NO", nullable = false, unique = true, length = 50)
    private String orderNo;

    @Column(name = "CUSTOMER_NAME", nullable = false, length = 100)
    private String customerName;

    @Column(name = "CUSTOMER_EMAIL", length = 100)
    private String customerEmail;

    @Column(name = "ORDER_DATE", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "TOTAL_AMOUNT", nullable = false)
    private Long totalAmount;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status; // PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED

    @Column(name = "ORDER_TYPE", length = 20)
    private String orderType; // CONSULTATION(상담주문), CUSTOMER(고객주문)

    @Column(name = "SHIPPING_ADDRESS", length = 500)
    private String shippingAddress;

    @OneToMany(mappedBy = "ord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdItem> ordItems = new ArrayList<>();

    @OneToMany(mappedBy = "ord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "ord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderFee> orderFees = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = LocalDateTime.now();
        }
        if (orderType == null || orderType.trim().isEmpty()) {
            orderType = "CONSULTATION"; // 기본값: 상담주문
        }
    }
}
