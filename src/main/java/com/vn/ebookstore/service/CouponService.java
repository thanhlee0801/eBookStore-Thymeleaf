package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Optional;

public interface CouponService {
    Optional<Coupon> findByCode(String code);
    BigDecimal calculateDiscount(Coupon coupon, BigDecimal amount);
    boolean isValidForUse(Coupon coupon, BigDecimal amount);
    Coupon useCoupon(Coupon coupon);
    Page<Coupon> getAllCoupons(Pageable pageable);
    Page<Coupon> getCouponsByTypeAndStatus(String type, String status, Pageable pageable);
    String getStatus(Coupon coupon);
    Coupon save(Coupon coupon);
    Optional<Coupon> findById(Integer id);
    void deleteCoupon(Integer id); // Thêm phương thức deleteCoupon
}