package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.OrderFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderFeeRepository extends JpaRepository<OrderFee, Long> {

    List<OrderFee> findByOrdId(Long ordId);

    List<OrderFee> findByOrdIdAndFeeType(Long ordId, String feeType);
}
