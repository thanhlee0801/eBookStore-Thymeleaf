package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Coupon;
import com.vn.ebookstore.repository.CouponRepository;
import com.vn.ebookstore.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class CouponServiceImpl implements CouponService {
    
    @Autowired 
    private CouponRepository couponRepository;

    @Override
    public Optional<Coupon> findByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return Optional.empty();
        }
        return couponRepository.findByCodeAndIsActiveTrue(code.toUpperCase());
    }

    @Override
    @Transactional
    public Double calculateDiscount(Coupon coupon, Double amount) {
        if (!isValidForUse(coupon, amount)) {
            return 0.0;
        }

        Double discount = 0.0;
        try {
            if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
                discount = amount * (coupon.getDiscountValue() / 100);
                if (coupon.getMaxDiscount() != null) {
                    discount = Math.min(discount, coupon.getMaxDiscount());
                }
            } else {
                discount = Math.min(coupon.getDiscountValue(), amount);
            }
        } catch (Exception e) {
            return 0.0;
        }

        return Math.round(discount * 100.0) / 100.0; // Làm tròn đến 2 chữ số thập phân
    }

    @Override
    public boolean isValidForUse(Coupon coupon, Double amount) {
        if (coupon == null || !coupon.getIsActive()) {
            return false;
        }

        Date now = new Date();
        return coupon.getIsActive() &&
               now.after(coupon.getStartDate()) &&
               now.before(coupon.getEndDate()) &&
               (coupon.getUsageLimit() == null || coupon.getTimesUsed() < coupon.getUsageLimit()) &&
               (coupon.getMinPurchase() == null || amount >= coupon.getMinPurchase());
    }

    @Transactional
    public Coupon useCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon cannot be null");
        }

        // Validate if coupon can still be used
        if (!isValidForUse(coupon, 0.0)) {
            throw new RuntimeException("Coupon is no longer valid");
        }

        coupon.setTimesUsed(coupon.getTimesUsed() + 1);
        
        // Check if usage limit is reached
        if (coupon.getUsageLimit() != null && coupon.getTimesUsed() >= coupon.getUsageLimit()) {
            coupon.setIsActive(false);
        }

        return couponRepository.save(coupon);
    }
}
