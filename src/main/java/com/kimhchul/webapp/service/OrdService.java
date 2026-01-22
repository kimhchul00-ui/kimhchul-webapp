package com.kimhchul.webapp.service;

import com.kimhchul.webapp.entity.Ord;
import com.kimhchul.webapp.entity.OrdItem;
import com.kimhchul.webapp.entity.OrderFee;
import com.kimhchul.webapp.repository.OrdRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdService {

    private final OrdRepository ordRepository;

    public List<Ord> findAll() {
        return ordRepository.findAllOrderByOrderDateDesc();
    }

    public Optional<Ord> findById(Long id) {
        return ordRepository.findById(id);
    }

    public Optional<Ord> findByOrderNo(String orderNo) {
        return ordRepository.findByOrderNo(orderNo);
    }

    @Transactional
    public Ord save(Ord ord) {
        // OrdItem에 Ord 설정
        if (ord.getOrdItems() != null) {
            ord.getOrdItems().forEach(item -> item.setOrd(ord));
        }

        // Payment에 Ord 설정
        if (ord.getPayments() != null) {
            ord.getPayments().forEach(payment -> payment.setOrd(ord));
        }

        // OrderFee에 Ord 설정
        if (ord.getOrderFees() != null) {
            ord.getOrderFees().forEach(fee -> fee.setOrd(ord));
        }

        // 총 금액 계산 (상품 금액 + 추가 비용)
        Long itemAmount = 0L;
        if (ord.getOrdItems() != null && !ord.getOrdItems().isEmpty()) {
            itemAmount = ord.getOrdItems().stream()
                    .mapToLong(item -> {
                        if (item.getTotalPrice() == null) {
                            item.calculateTotalPrice();
                        }
                        return item.getTotalPrice() != null ? item.getTotalPrice() : 0L;
                    })
                    .sum();
        }

        Long feeAmount = 0L;
        if (ord.getOrderFees() != null && !ord.getOrderFees().isEmpty()) {
            feeAmount = ord.getOrderFees().stream()
                    .mapToLong(fee -> fee.getAmount() != null ? fee.getAmount() : 0L)
                    .sum();
        }

        ord.setTotalAmount(itemAmount + feeAmount);

        return ordRepository.save(ord);
    }

    @Transactional
    public Ord update(Long id, Ord ord) {
        Ord existingOrd = ordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + id));

        existingOrd.setCustomerName(ord.getCustomerName());
        existingOrd.setCustomerEmail(ord.getCustomerEmail());
        existingOrd.setStatus(ord.getStatus());
        existingOrd.setShippingAddress(ord.getShippingAddress());

        // 기존 아이템 삭제 후 새로 추가
        existingOrd.getOrdItems().clear();
        if (ord.getOrdItems() != null) {
            ord.getOrdItems().forEach(item -> {
                item.setOrd(existingOrd);
                existingOrd.getOrdItems().add(item);
            });
        }

        // Payment 업데이트 (기존 Payment는 유지, 새로운 것만 추가)
        if (ord.getPayments() != null) {
            ord.getPayments().forEach(payment -> {
                payment.setOrd(existingOrd);
                if (payment.getId() == null) {
                    existingOrd.getPayments().add(payment);
                }
            });
        }

        // OrderFee 업데이트
        existingOrd.getOrderFees().clear();
        if (ord.getOrderFees() != null) {
            ord.getOrderFees().forEach(fee -> {
                fee.setOrd(existingOrd);
                existingOrd.getOrderFees().add(fee);
            });
        }

        // 총 금액 재계산 (상품 금액 + 추가 비용)
        Long itemAmount = existingOrd.getOrdItems().stream()
                .mapToLong(item -> {
                    item.calculateTotalPrice();
                    return item.getTotalPrice();
                })
                .sum();

        Long feeAmount = existingOrd.getOrderFees().stream()
                .mapToLong(OrderFee::getAmount)
                .sum();

        existingOrd.setTotalAmount(itemAmount + feeAmount);

        return ordRepository.save(existingOrd);
    }

    @Transactional
    public void delete(Long id) {
        ordRepository.deleteById(id);
    }

    public List<Ord> findByStatus(String status) {
        return ordRepository.findByStatus(status);
    }

    public List<Ord> searchByCustomerName(String customerName) {
        return ordRepository.findByCustomerNameContainingIgnoreCase(customerName);
    }

    public Page<Ord> findWithFilters(String status, String customerName, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        boolean hasStatus = status != null && !status.trim().isEmpty();
        boolean hasCustomerName = customerName != null && !customerName.trim().isEmpty();
        boolean hasDateRange = startDate != null && endDate != null;

        if (hasDateRange) {
            if (hasStatus && hasCustomerName) {
                return ordRepository.findByStatusAndCustomerNameContainingAndOrderDateBetween(
                    status, customerName, startDate, endDate, pageable);
            } else if (hasStatus) {
                return ordRepository.findByStatusAndOrderDateBetween(status, startDate, endDate, pageable);
            } else if (hasCustomerName) {
                return ordRepository.findByCustomerNameContainingAndOrderDateBetween(
                    customerName, startDate, endDate, pageable);
            } else {
                return ordRepository.findByOrderDateBetween(startDate, endDate, pageable);
            }
        } else {
            // 날짜 범위가 없을 때
            if (hasStatus && hasCustomerName) {
                return ordRepository.findByStatusAndCustomerNameContaining(status, customerName, pageable);
            } else if (hasStatus) {
                return ordRepository.findByStatus(status, pageable);
            } else if (hasCustomerName) {
                return ordRepository.findByCustomerNameContaining(customerName, pageable);
            } else {
                return ordRepository.findAllOrderByOrderDateDesc(pageable);
            }
        }
    }
}
