package com.kimhchul.webapp.controller;

import com.kimhchul.webapp.entity.Item;
import com.kimhchul.webapp.entity.Uitem;
import com.kimhchul.webapp.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/admin/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @Value("${app.version:dev}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String search,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        
        Page<Item> itemPage;
        if (search != null && !search.trim().isEmpty()) {
            itemPage = itemService.searchItems(search, status, pageable);
        } else if (status != null && !status.trim().isEmpty()) {
            itemPage = itemService.findAll(pageable);
            // 필터링은 서비스에서 처리하도록 수정 필요하지만, 일단 클라이언트 사이드 필터링으로 처리
        } else {
            itemPage = itemService.findAll(pageable);
        }
        
        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("totalElements", itemPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("serverPort", serverPort);
        
        return "admin/items/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Item item = new Item();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
        String timeStr = now.format(formatter);
        
        item.setItemCode("ITEM-" + timeStr.replace(":", ""));
        item.setItemName("새 상품");
        item.setDescription("상품 설명을 입력하세요");
        item.setPrice(10000L);
        item.setStockQuantity(100);
        item.setStatus("ACTIVE");
        item.setImageUrl("");
        
        item.setUitems(new ArrayList<>());
        Uitem uitem = new Uitem();
        uitem.setOptionName("색상");
        uitem.setOptionValue("기본");
        uitem.setAdditionalPrice(0L);
        uitem.setStockQuantity(100);
        uitem.setStatus("ACTIVE");
        item.getUitems().add(uitem);
        
        model.addAttribute("item", item);
        return "admin/items/form";
    }

    @PostMapping
    public String create(@ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        if (item.getItemCode() == null || item.getItemCode().trim().isEmpty()) {
            item.setItemCode("ITEM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        if (item.getUitems() == null) {
            item.setUitems(new ArrayList<>());
        }
        
        itemService.save(item);
        redirectAttributes.addFlashAttribute("message", "상품이 등록되었습니다.");
        return "redirect:/admin/items";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));
        model.addAttribute("item", item);
        return "admin/items/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));
        model.addAttribute("item", item);
        return "admin/items/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Item item, RedirectAttributes redirectAttributes) {
        itemService.update(id, item);
        redirectAttributes.addFlashAttribute("message", "상품이 수정되었습니다.");
        return "redirect:/admin/items";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        itemService.delete(id);
        redirectAttributes.addFlashAttribute("message", "상품이 삭제되었습니다.");
        return "redirect:/admin/items";
    }
}
