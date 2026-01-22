package com.kimhchul.webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ORDER_FEE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORD_ID", nullable = false)
    private Ord ord;

    @Column(name = "FEE_TYPE", nullable = false, length = 50)
    private String feeType; // SHIPPING(배송비), PACKAGING(포장비), TIP(팁), ETC(기타)

    @Column(name = "FEE_NAME", nullable = false, length = 100)
    private String feeName; // 비용명 (예: "일반배송비", "특급배송비" 등)

    @Column(name = "AMOUNT", nullable = false)
    private Long amount; // 비용 금액

    @Column(name = "DESCRIPTION", length = 500)
    private String description; // 비용 설명
}
