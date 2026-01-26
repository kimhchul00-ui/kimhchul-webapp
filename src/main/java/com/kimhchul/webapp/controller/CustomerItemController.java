package com.kimhchul.webapp.controller;

import com.kimhchul.webapp.entity.Item;
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

@Controller
@RequestMapping("/customer/items")
@RequiredArgsConstructor
public class CustomerItemController {

    private final ItemService itemService;

    @Value("${app.version:dev}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping
    public String list(Model model,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "12") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdDate"));
        
        Page<Item> itemPage = itemService.findAllActive(pageable);
        
        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", itemPage.getTotalPages());
        model.addAttribute("totalElements", itemPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("serverPort", serverPort);
        
        return "customer/items/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Item item = itemService.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));
        model.addAttribute("item", item);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("serverPort", serverPort);
        return "customer/items/detail";
    }

    @PostMapping("/{id}/buy-now")
    public String buyNow(@PathVariable Long id,
                        @RequestParam(required = false) Long uitemId,
                        @RequestParam(defaultValue = "1") Integer quantity,
                        RedirectAttributes redirectAttributes) {
        
        // 상품 존재 여부 확인
        itemService.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다: " + id));
        
        // 주문서로 리다이렉트하면서 상품 정보를 전달
        redirectAttributes.addFlashAttribute("buyNowItemId", id);
        redirectAttributes.addFlashAttribute("buyNowUitemId", uitemId);
        redirectAttributes.addFlashAttribute("buyNowQuantity", quantity);
        
        return "redirect:/customer/orders/new";
    }
}
