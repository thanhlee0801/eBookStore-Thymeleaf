package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Coupon;
import java.util.Optional;

public interface CouponService {
    Optional<Coupon> findByCode(String code);
    Double calculateDiscount(Coupon coupon, Double amount);
    boolean isValidForUse(Coupon coupon, Double amount); 
    Coupon useCoupon(Coupon coupon);
}
