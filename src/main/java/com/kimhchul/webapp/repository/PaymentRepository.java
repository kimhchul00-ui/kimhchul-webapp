package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrdId(Long ordId);

    List<Payment> findByOrdIdAndPaymentStatus(Long ordId, String paymentStatus);
}
