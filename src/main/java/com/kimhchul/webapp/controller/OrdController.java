package com.kimhchul.webapp.controller;

import com.kimhchul.webapp.entity.Ord;
import com.kimhchul.webapp.entity.OrdItem;
import com.kimhchul.webapp.entity.OrderFee;
import com.kimhchul.webapp.entity.Payment;
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
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdController {

    private final OrdService ordService;

    @Value("${app.version:dev}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String search,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size) {
        
        // 페이징 설정
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "orderDate"));
        
        // 날짜 범위 설정
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        if (startDate != null) {
            startDateTime = startDate.atStartOfDay();
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(LocalTime.MAX);
        }
        
        // 조회 실행
        Page<Ord> orderPage = ordService.findWithFilters(status, search, startDateTime, endDateTime, pageable);
        
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("status", status);
        model.addAttribute("search", search);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("serverPort", serverPort);
        
        return "orders/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Ord ord = new Ord();
        
        // 현재 시간 기반으로 기본값 설정
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("mm:ss");
        String timeStr = now.format(formatter);
        
        // 고객명: "테스트_" + mm:ss
        ord.setCustomerName("테스트_" + timeStr);
        
        // 이메일: "test" + mmss (콜론과 언더스코어 제거) + "@test.com"
        String emailTimeStr = timeStr.replace(":", ""); // 콜론 제거
        ord.setCustomerEmail("test" + emailTimeStr + "@test.com");
        
        // 상태: 랜덤하게 선택
        List<String> statuses = Arrays.asList("PENDING", "CONFIRMED", "SHIPPED", "DELIVERED");
        Random random = new Random();
        ord.setStatus(statuses.get(random.nextInt(statuses.size())));
        
        // 배송주소: 이름 + " " + 이메일
        ord.setShippingAddress(ord.getCustomerName() + " " + ord.getCustomerEmail());
        
        // 주문 상품 기본값 설정
        ord.setOrdItems(new ArrayList<>());
        OrdItem item = new OrdItem();
        item.setProductName("테스트상품1");
        item.setProductCode("T0001");
        item.setQuantity(1);
        item.setUnitPrice(10000L);
        ord.getOrdItems().add(item);
        
        // 결제 기본값 설정
        ord.setPayments(new ArrayList<>());
        Payment payment = new Payment();
        payment.setPaymentType("CARD");
        payment.setPaymentMethod("신용카드");
        payment.setAmount(10000L);
        payment.setPaymentStatus("PAYMENT");
        ord.getPayments().add(payment);
        
        // 비용 기본값 설정
        ord.setOrderFees(new ArrayList<>());
        OrderFee fee = new OrderFee();
        fee.setFeeType("SHIPPING");
        fee.setFeeName("일반배송비");
        fee.setAmount(3000L);
        ord.getOrderFees().add(fee);
        
        model.addAttribute("ord", ord);
        return "orders/form";
    }

    @PostMapping
    public String create(@ModelAttribute Ord ord, RedirectAttributes redirectAttributes) {
        // 주문번호 자동 생성
        if (ord.getOrderNo() == null || ord.getOrderNo().trim().isEmpty()) {
            ord.setOrderNo("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        
        ordService.save(ord);
        redirectAttributes.addFlashAttribute("message", "주문이 생성되었습니다.");
        return "redirect:/orders";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        Ord ord = ordService.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + id));
        model.addAttribute("ord", ord);
        return "orders/detail";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Ord ord = ordService.findById(id)
                .orElseThrow(() -> new RuntimeException("주문을 찾을 수 없습니다: " + id));
        model.addAttribute("ord", ord);
        return "orders/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Ord ord, RedirectAttributes redirectAttributes) {
        ordService.update(id, ord);
        redirectAttributes.addFlashAttribute("message", "주문이 수정되었습니다.");
        return "redirect:/orders";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ordService.delete(id);
        redirectAttributes.addFlashAttribute("message", "주문이 삭제되었습니다.");
        return "redirect:/orders";
    }

    @GetMapping("/home")
    public String home() {
        return "redirect:/orders";
    }
}
