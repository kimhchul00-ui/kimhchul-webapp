package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findByItemCode(String itemCode);
    
    List<Item> findByStatus(String status);
    
    Page<Item> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.status = 'ACTIVE' ORDER BY i.createdDate DESC")
    List<Item> findAllActiveItems();
    
    @Query("SELECT i FROM Item i WHERE i.status = 'ACTIVE' ORDER BY i.createdDate DESC")
    Page<Item> findAllActiveItems(Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE " +
           "(LOWER(i.itemName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:status IS NULL OR i.status = :status)")
    Page<Item> searchItems(String search, String status, Pageable pageable);
}
