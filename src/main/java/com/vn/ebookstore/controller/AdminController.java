package com.vn.ebookstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
    public String home() {
        return "page/admin/dashboard";
    }

    // Thêm các endpoint khác cho admin ở đây
}
