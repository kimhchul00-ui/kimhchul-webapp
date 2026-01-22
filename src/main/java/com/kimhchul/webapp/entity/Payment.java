package com.kimhchul.webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORD_ID", nullable = false)
    private Ord ord;

    @Column(name = "PAYMENT_TYPE", nullable = false, length = 20)
    private String paymentType; // CARD, BANK_TRANSFER, MOBILE, CASH 등

    @Column(name = "PAYMENT_METHOD", nullable = false, length = 50)
    private String paymentMethod; // 결제수단 상세 (예: 신용카드, 계좌이체 등)

    @Column(name = "AMOUNT", nullable = false)
    private Long amount; // 결제 금액

    @Column(name = "PAYMENT_STATUS", nullable = false, length = 20)
    private String paymentStatus; // PAYMENT(결제), REFUND(환불), PARTIAL_REFUND(부분환불)

    @Column(name = "PAYMENT_DATE", nullable = false)
    private LocalDateTime paymentDate;

    @Column(name = "REFUND_DATE")
    private LocalDateTime refundDate;

    @Column(name = "REFUND_REASON", length = 500)
    private String refundReason; // 환불 사유

    @Column(name = "TRANSACTION_ID", length = 100)
    private String transactionId; // 거래 ID (PG사 거래번호 등)

    @PrePersist
    protected void onCreate() {
        if (paymentDate == null) {
            paymentDate = LocalDateTime.now();
        }
    }
}
