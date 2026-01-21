package com.kimhchul.webapp.repository;

import com.kimhchul.webapp.entity.OrdItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdItemRepository extends JpaRepository<OrdItem, Long> {

    List<OrdItem> findByOrdId(Long ordId);
}
