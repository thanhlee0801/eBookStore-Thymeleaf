package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CouponService {
    Optional<Coupon> findByCode(String code);
    Double calculateDiscount(Coupon coupon, Double amount);
    boolean isValidForUse(Coupon coupon, Double amount);
    Coupon useCoupon(Coupon coupon);

    // Thêm phương thức mới
    Page<Coupon> getAllCoupons(Pageable pageable);
    Page<Coupon> getCouponsByTypeAndStatus(String type, String status, Pageable pageable);
    String getStatus(Coupon coupon); // Thêm để tính trạng thái
}