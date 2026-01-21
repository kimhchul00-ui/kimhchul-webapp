package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.Ord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdRepository extends JpaRepository<Ord, Long> {

    Optional<Ord> findByOrderNo(String orderNo);

    List<Ord> findByStatus(String status);

    @Query("SELECT o FROM Ord o ORDER BY o.orderDate DESC")
    List<Ord> findAllOrderByOrderDateDesc();

    @Query("SELECT o FROM Ord o ORDER BY o.orderDate DESC")
    Page<Ord> findAllOrderByOrderDateDesc(Pageable pageable);

    List<Ord> findByCustomerNameContainingIgnoreCase(String customerName);

    @Query("SELECT o FROM Ord o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate ORDER BY o.orderDate DESC")
    Page<Ord> findByOrderDateBetween(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate, 
                                      Pageable pageable);

    @Query("SELECT o FROM Ord o WHERE o.status = :status AND o.orderDate >= :startDate AND o.orderDate < :endDate ORDER BY o.orderDate DESC")
    Page<Ord> findByStatusAndOrderDateBetween(@Param("status") String status,
                                               @Param("startDate") LocalDateTime startDate,
                                               @Param("endDate") LocalDateTime endDate,
                                               Pageable pageable);

    @Query("SELECT o FROM Ord o WHERE o.customerName LIKE %:customerName% AND o.orderDate >= :startDate AND o.orderDate < :endDate ORDER BY o.orderDate DESC")
    Page<Ord> findByCustomerNameContainingAndOrderDateBetween(@Param("customerName") String customerName,
                                                                @Param("startDate") LocalDateTime startDate,
                                                                @Param("endDate") LocalDateTime endDate,
                                                                Pageable pageable);

    @Query("SELECT o FROM Ord o WHERE o.status = :status AND o.customerName LIKE %:customerName% AND o.orderDate >= :startDate AND o.orderDate < :endDate ORDER BY o.orderDate DESC")
    Page<Ord> findByStatusAndCustomerNameContainingAndOrderDateBetween(@Param("status") String status,
                                                                        @Param("customerName") String customerName,
                                                                        @Param("startDate") LocalDateTime startDate,
                                                                        @Param("endDate") LocalDateTime endDate,
                                                                        Pageable pageable);

    @Query("SELECT o FROM Ord o WHERE o.status = :status ORDER BY o.orderDate DESC")
    Page<Ord> findByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT o FROM Ord o WHERE o.customerName LIKE %:customerName% ORDER BY o.orderDate DESC")
    Page<Ord> findByCustomerNameContaining(@Param("customerName") String customerName, Pageable pageable);

    @Query("SELECT o FROM Ord o WHERE o.status = :status AND o.customerName LIKE %:customerName% ORDER BY o.orderDate DESC")
    Page<Ord> findByStatusAndCustomerNameContaining(@Param("status") String status,
                                                      @Param("customerName") String customerName,
                                                      Pageable pageable);
}
