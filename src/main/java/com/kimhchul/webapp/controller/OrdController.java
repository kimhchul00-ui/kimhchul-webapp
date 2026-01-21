package com.kimhchul.webapp.controller;

import com.kimhchul.webapp.entity.Ord;
import com.kimhchul.webapp.entity.OrdItem;
import com.kimhchul.webapp.service.OrdService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrdController {

    private final OrdService ordService;

    @GetMapping
    public String list(Model model,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) String search) {
        List<Ord> orders;
        
        if (search != null && !search.trim().isEmpty()) {
            orders = ordService.searchByCustomerName(search);
        } else if (status != null && !status.trim().isEmpty()) {
            orders = ordService.findByStatus(status);
        } else {
            orders = ordService.findAll();
        }
        
        model.addAttribute("orders", orders);
        return "orders/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        Ord ord = new Ord();
        ord.setOrdItems(new ArrayList<>());
        ord.getOrdItems().add(new OrdItem());
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
