package com.vn.ebookstore.repository;

import com.vn.ebookstore.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Integer>, JpaSpecificationExecutor<Coupon> {
    Optional<Coupon> findByCodeAndIsActiveTrue(String code);
    boolean existsByCode(String code);

    // Thêm phương thức phân trang theo loại
    Page<Coupon> findByDiscountType(String discountType, Pageable pageable);

    // Thêm truy vấn tùy chỉnh cho trạng thái
    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND CURRENT_DATE BETWEEN c.startDate AND c.endDate")
    Page<Coupon> findActiveCoupons(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.isActive = true AND c.endDate < CURRENT_DATE")
    Page<Coupon> findExpiredCoupons(Pageable pageable);

    @Query("SELECT c FROM Coupon c WHERE c.isActive = false")
    Page<Coupon> findInactiveCoupons(Pageable pageable);
}