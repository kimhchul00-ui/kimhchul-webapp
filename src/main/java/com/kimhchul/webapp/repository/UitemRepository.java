package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.Item;
import com.kimhchul.webapp.entity.Uitem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UitemRepository extends JpaRepository<Uitem, Long> {
    List<Uitem> findByItem(Item item);
    
    List<Uitem> findByItemAndStatus(Item item, String status);
}
