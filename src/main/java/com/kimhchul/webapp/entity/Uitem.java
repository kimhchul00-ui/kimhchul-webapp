package com.kimhchul.webapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "UITEM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Uitem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Column(name = "OPTION_NAME", nullable = false, length = 100)
    private String optionName;

    @Column(name = "OPTION_VALUE", nullable = false, length = 100)
    private String optionValue;

    @Column(name = "ADDITIONAL_PRICE", nullable = false)
    private Long additionalPrice; // 옵션 추가 가격

    @Column(name = "STOCK_QUANTITY", nullable = false)
    private Integer stockQuantity;

    @Column(name = "STATUS", nullable = false, length = 20)
    private String status; // ACTIVE, INACTIVE, SOLD_OUT

    @PrePersist
    protected void onCreate() {
        if (additionalPrice == null) {
            additionalPrice = 0L;
        }
        if (stockQuantity == null) {
            stockQuantity = 0;
        }
        if (status == null || status.trim().isEmpty()) {
            status = "ACTIVE";
        }
    }
}
