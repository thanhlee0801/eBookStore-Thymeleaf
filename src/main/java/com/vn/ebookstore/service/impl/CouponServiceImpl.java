package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Coupon;
import com.vn.ebookstore.repository.CouponRepository;
import com.vn.ebookstore.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    public BigDecimal calculateDiscount(Coupon coupon, BigDecimal amount) {
        if (!isValidForUse(coupon, amount)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;
        try {
            if (coupon.getDiscountType() == Coupon.DiscountType.PERCENTAGE) {
                discount = amount.multiply(coupon.getDiscountValue().divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP));
                if (coupon.getMaxDiscount() != null) {
                    discount = discount.min(coupon.getMaxDiscount());
                }
            } else {
                discount = coupon.getDiscountValue().min(amount);
            }
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }

        return discount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    @Override
    public boolean isValidForUse(Coupon coupon, BigDecimal amount) {
        if (coupon == null || !coupon.getIsActive()) {
            return false;
        }

        Date now = new Date();
        return coupon.getIsActive() &&
                now.after(coupon.getStartDate()) &&
                now.before(coupon.getEndDate()) &&
                (coupon.getUsageLimit() == null || coupon.getTimesUsed() < coupon.getUsageLimit()) &&
                (coupon.getMinPurchase() == null || amount.compareTo(coupon.getMinPurchase()) >= 0);
    }

    @Override
    @Transactional
    public Coupon useCoupon(Coupon coupon) {
        if (coupon == null) {
            throw new IllegalArgumentException("Coupon cannot be null");
        }

        if (!isValidForUse(coupon, BigDecimal.ZERO)) {
            throw new RuntimeException("Coupon is no longer valid");
        }

        coupon.setTimesUsed(coupon.getTimesUsed() + 1);

        if (coupon.getUsageLimit() != null && coupon.getTimesUsed() >= coupon.getUsageLimit()) {
            coupon.setIsActive(false);
        }

        return couponRepository.save(coupon);
    }

    @Override
    public Page<Coupon> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    @Override
    public Page<Coupon> getCouponsByTypeAndStatus(String type, String status, Pageable pageable) {
        Date now = new Date();
        if ("active".equals(status)) {
            return couponRepository.findActiveCoupons(pageable);
        } else if ("expired".equals(status)) {
            return couponRepository.findExpiredCoupons(pageable);
        } else if ("inactive".equals(status)) {
            return couponRepository.findInactiveCoupons(pageable);
        } else if (type != null) {
            return couponRepository.findByDiscountType(type, pageable);
        }
        return getAllCoupons(pageable);
    }

    @Override
    public String getStatus(Coupon coupon) {
        Date now = new Date();
        if (!coupon.getIsActive()) {
            return "inactive";
        }
        if (now.before(coupon.getStartDate())) {
            return "not_started";
        }
        if (now.after(coupon.getEndDate())) {
            return "expired";
        }
        long diffInMillies = Math.abs(coupon.getEndDate().getTime() - now.getTime());
        long diffInDays = diffInMillies / (24 * 60 * 60 * 1000);
        if (diffInDays <= 7) {
            return "soon_to_expire";
        }
        return "active";
    }

    @Override
    @Transactional
    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    @Override
    public Optional<Coupon> findById(Integer id) {
        return couponRepository.findById(id);
    }

    @Override
    @Transactional
    public void deleteCoupon(Integer id) {
        couponRepository.deleteById(id);
    }
}