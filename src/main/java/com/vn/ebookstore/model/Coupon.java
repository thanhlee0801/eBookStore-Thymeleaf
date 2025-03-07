package com.vn.ebookstore.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;
    
    private String description;
    
    @Column(name = "discount_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;
    
    @Column(name = "discount_value", nullable = false)
    private Double discountValue;
    
    @Column(name = "min_purchase")
    private Double minPurchase;
    
    @Column(name = "max_discount")
    private Double maxDiscount;
    
    @Column(name = "start_date", nullable = false)
    private Date startDate;
    
    @Column(name = "end_date", nullable = false)
    private Date endDate;
    
    @Column(name = "usage_limit")
    private Integer usageLimit;
    
    @Column(name = "times_used")
    private Integer timesUsed = 0;
    
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @OneToMany(mappedBy = "coupon")
    private List<OrderDetail> orders;

    @OneToMany(mappedBy = "coupon")
    private List<UserCoupon> userCoupons;

    public enum DiscountType {
        PERCENTAGE,
        FIXED_AMOUNT
    }

    // Add getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code; 
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DiscountType getDiscountType() {
        return discountType;
    }

    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public Double getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(Double minPurchase) {
        this.minPurchase = minPurchase;
    }

    public Double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(Double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(Integer usageLimit) {
        this.usageLimit = usageLimit;
    }

    public Integer getTimesUsed() {
        return timesUsed;
    }

    public void setTimesUsed(Integer timesUsed) {
        this.timesUsed = timesUsed;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderDetail> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetail> orders) {
        this.orders = orders;
    }

    public List<UserCoupon> getUserCoupons() {
        return userCoupons;
    }

    public void setUserCoupons(List<UserCoupon> userCoupons) {
        this.userCoupons = userCoupons;
    }
}
