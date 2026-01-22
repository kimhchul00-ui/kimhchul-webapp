package com.kimhchul.webapp.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Value("${app.version:dev}")
    private String appVersion;

    @Value("${server.port:8080}")
    private String serverPort;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("serverPort", serverPort);
        return "index";
    }
}
