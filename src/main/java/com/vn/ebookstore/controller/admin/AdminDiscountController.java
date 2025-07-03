package com.vn.ebookstore.controller.admin;

import com.vn.ebookstore.model.Coupon;
import com.vn.ebookstore.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/admin/coupons")
public class AdminDiscountController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public String listDiscounts(@RequestParam(required = false) String type,
                                @RequestParam(required = false) String status,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Coupon> couponsPage = couponService.getCouponsByTypeAndStatus(type, status, pageable);
        model.addAttribute("coupons", couponsPage);
        model.addAttribute("types", Arrays.asList("PERCENTAGE", "FIXED_AMOUNT"));
        model.addAttribute("statuses", Arrays.asList("active", "inactive", "expired", "soon_to_expire"));
        return "page/admin/discounts/list-discounts";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        model.addAttribute("types", Arrays.asList("PERCENTAGE", "FIXED_AMOUNT"));
        return "page/admin/discounts/create-discount";
    }

    @PostMapping("/save")
    public String saveCoupon(@ModelAttribute Coupon coupon) {
        couponService.save(coupon);
        return "redirect:/admin/discounts?success=created";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Coupon coupon = couponService.findById(id).orElse(null);
        if (coupon == null) {
            return "redirect:/admin/discounts?error=not-found";
        }
        model.addAttribute("coupon", coupon);
        model.addAttribute("types", Arrays.asList("PERCENTAGE", "FIXED_AMOUNT"));
        return "page/admin/discounts/create-discount";
    }

    @PostMapping("/update")
    public String updateCoupon(@ModelAttribute Coupon coupon) {
        Coupon existingCoupon = couponService.findById(coupon.getId()).orElse(null);
        if (existingCoupon == null) {
            return "redirect:/admin/discounts?error=not-found";
        }
        couponService.save(coupon);
        return "redirect:/admin/discounts?success=updated";
    }

    @GetMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable Integer id) {
        Coupon coupon = couponService.findById(id).orElse(null);
        if (coupon != null) {
            couponService.deleteCoupon(id);
        }
        return "redirect:/admin/discounts?success=deleted";
    }
}