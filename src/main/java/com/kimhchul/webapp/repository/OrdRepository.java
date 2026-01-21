package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.Ord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdRepository extends JpaRepository<Ord, Long> {

    Optional<Ord> findByOrderNo(String orderNo);

    List<Ord> findByStatus(String status);

    @Query("SELECT o FROM Ord o ORDER BY o.orderDate DESC")
    List<Ord> findAllOrderByOrderDateDesc();

    List<Ord> findByCustomerNameContainingIgnoreCase(String customerName);
}
