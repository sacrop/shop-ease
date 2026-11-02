package com.shop.ease.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.shop.ease.Util.CouponStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;


@Entity

@Table
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Coupon code is required")
    private String couponCode;

    @Min(value = 0, message = "Discount percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Discount percentage must be less than or equal to 100")
    private double discountPercentage;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "coupon_status")
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Min(value = 0, message = "Minimum purchase must be greater than or equal to 0")
    private double minimumPurchase;

    private String couponDescription;

    @OneToMany(mappedBy = "coupon")
    private List<CouponUsage> couponUsages = new ArrayList<>();

    @Override
    public String toString() {
        return "Coupon [couponId=" + couponId + ", couponCode=" + couponCode + ", discountPercentage=" + discountPercentage + ", startDate="
                + startDate + ", endDate=" + endDate + ", couponStatus=" + couponStatus + ", minimumPurchase=" + minimumPurchase
                + ", couponDescription=" + couponDescription + "]";
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public CouponStatus getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(CouponStatus couponStatus) {
        this.couponStatus = couponStatus;
    }

    public double getMinimumPurchase() {
        return minimumPurchase;
    }

    public void setMinimumPurchase(double minimumPurchase) {
        this.minimumPurchase = minimumPurchase;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public List<CouponUsage> getCouponUsages() {
        return couponUsages;
    }

    public void setCouponUsages(List<CouponUsage> couponUsages) {
        this.couponUsages = couponUsages;
    }

    

   

   
}
