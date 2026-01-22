package com.kimhchul.webapp.controller;

import com.kimhchul.webapp.entity.Ord;
import com.kimhchul.webapp.entity.OrdItem;
import com.kimhchul.webapp.entity.OrderFee;
import com.kimhchul.webapp.service.OrdService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

@Controller
@RequestMapping("/customer/orders")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final OrdService ordService;

    @Value("${app.version:dev}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping
    public String list(Model model,
                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate"));
        
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        }
        
        Page<Ord> orderPage = ordService.findWithFilters(null, null, startDateTime, endDateTime, pageable);
        
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("serverPort", serverPort);
        
        return "customer/orders/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Ord ord = new Ord();
        
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
        String timeStr = now.format(formatter);
        
        ord.setCustomerName("테스트_" + timeStr);
        String emailTimeStr = timeStr.replace(":", "");
        ord.setCustomerEmail("test" + emailTimeStr + "@test.com");
        ord.setStatus("PENDING");
        ord.setShippingAddress("");
        
        ord.setOrdItems(new ArrayList<>());
        OrdItem item = new OrdItem();
        item.setProductName("테스트상품1");
        item.setProductCode("T0001");
        item.setQuantity(1);
        item.setUnitPrice(10000L);
        ord.getOrdItems().add(item);
        
        ord.setOrderFees(new ArrayList<>());
        OrderFee fee = new OrderFee();
        fee.setFeeType("SHIPPING");
        fee.setFeeName("일반배송비");
        fee.setAmount(3000L);
        ord.getOrderFees().add(fee);
        
        ord.setPayments(new ArrayList<>());
        
        model.addAttribute("ord", ord);
        return "customer/orders/new";
    }

    @PostMapping
    public String create(@ModelAttribute Ord ord, RedirectAttributes redirectAttributes) {
        if (ord.getOrderNo() == null || ord.getOrderNo().trim().isEmpty()) {
            ord.setOrderNo("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        // null 체크 및 초기화
        if (ord.getOrdItems() == null) {
            ord.setOrdItems(new ArrayList<>());
        }
        if (ord.getPayments() == null) {
            ord.setPayments(new ArrayList<>());
        }
        if (ord.getOrderFees() == null) {
            ord.setOrderFees(new ArrayList<>());
        }
        
        // 상태 기본값 설정
        if (ord.getStatus() == null || ord.getStatus().trim().isEmpty()) {
            ord.setStatus("PENDING");
        }
        
        ordService.save(ord);
        redirectAttributes.addFlashAttribute("message", "주문이 완료되었습니다.");
        return "redirect:/customer/orders";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Ord ord = ordService.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + id));
        model.addAttribute("ord", ord);
        return "customer/orders/detail";
    }
}
