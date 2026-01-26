package com.kimhchul.webapp.service;

import com.kimhchul.webapp.entity.Item;
import com.kimhchul.webapp.entity.Uitem;
import com.kimhchul.webapp.repository.ItemRepository;
import com.kimhchul.webapp.repository.UitemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;
    private final UitemRepository uitemRepository;

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public List<Item> findAllActive() {
        return itemRepository.findAllActiveItems();
    }

    public Page<Item> findAllActive(Pageable pageable) {
        return itemRepository.findAllActiveItems(pageable);
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Optional<Item> findByItemCode(String itemCode) {
        return itemRepository.findByItemCode(itemCode);
    }

    public Page<Item> searchItems(String search, String status, Pageable pageable) {
        return itemRepository.searchItems(search, status, pageable);
    }

    @Transactional
    public Item save(Item item) {
        // Uitem에 Item 설정
        if (item.getUitems() != null) {
            item.getUitems().forEach(uitem -> uitem.setItem(item));
        }
        return itemRepository.save(item);
    }

    @Transactional
    public Item update(Long id, Item item) {
        Item existingItem = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));

        existingItem.setItemName(item.getItemName());
        existingItem.setDescription(item.getDescription());
        existingItem.setPrice(item.getPrice());
        existingItem.setStockQuantity(item.getStockQuantity());
        existingItem.setStatus(item.getStatus());
        existingItem.setImageUrl(item.getImageUrl());

        // 기존 Uitem 삭제 후 새로 추가
        existingItem.getUitems().clear();
        if (item.getUitems() != null) {
            item.getUitems().forEach(uitem -> {
                uitem.setItem(existingItem);
                existingItem.getUitems().add(uitem);
            });
        }

        return itemRepository.save(existingItem);
    }

    @Transactional
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Transactional
    public void decreaseStock(Long itemId, Integer quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + itemId));
        
        if (item.getStockQuantity() < quantity) {
            throw new RuntimeException("재고가 부족합니다. 현재 재고: " + item.getStockQuantity());
        }
        
        item.setStockQuantity(item.getStockQuantity() - quantity);
        itemRepository.save(item);
    }

    @Transactional
    public void decreaseStock(Long itemId, Long uitemId, Integer quantity) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + itemId));
        
        if (uitemId != null) {
            Uitem uitem = uitemRepository.findById(uitemId)
                    .orElseThrow(() -> new RuntimeException("옵션을 찾을 수 없습니다: " + uitemId));
            
            if (uitem.getStockQuantity() < quantity) {
                throw new RuntimeException("옵션 재고가 부족합니다. 현재 재고: " + uitem.getStockQuantity());
            }
            
            uitem.setStockQuantity(uitem.getStockQuantity() - quantity);
            uitemRepository.save(uitem);
        } else {
            if (item.getStockQuantity() < quantity) {
                throw new RuntimeException("재고가 부족합니다. 현재 재고: " + item.getStockQuantity());
            }
            
            item.setStockQuantity(item.getStockQuantity() - quantity);
            itemRepository.save(item);
        }
    }
}
