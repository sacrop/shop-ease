package com.shop.ease.dto;

import java.time.LocalDate;
import java.util.List;

import com.shop.ease.Util.CouponStatus;
import com.shop.ease.model.CouponUsage;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;



public class CouponDto {

    private Long couponId;
    

    @NotBlank(message = "Coupon code is required")
    private String couponCode;

    @NotNull(message = "discountPercentage cannot be null")
    @Min(value = 0, message = "Discount percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Discount percentage must be less than or equal to 100")
    private double discountPercentage;

    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    
    @Enumerated(EnumType.STRING)
    private CouponStatus couponStatus;

    @Min(value = 0, message = "Minimum purchase must be greater than or equal to 0")
    private double minimumPurchase;

    private String couponDescription;

   
    private List<CouponUsage> couponUsages;


    private CouponDto(Builder builder) {
        this.couponId = builder.couponId;
        this.couponCode = builder.couponCode;
        this.discountPercentage = builder.discountPercentage;
        this.startDate = builder.startDate;
        this.endDate = builder.endDate;
        this.couponStatus = builder.couponStatus;
        this.minimumPurchase = builder.minimumPurchase;
        this.couponDescription = builder.couponDescription;
        this.couponUsages = builder.couponUsages;
    }

    public static class Builder {
        private Long couponId;
        private String couponCode;
        private double discountPercentage;
        private LocalDate startDate;
        private LocalDate endDate;
        private CouponStatus couponStatus;
        private double minimumPurchase;
        private String couponDescription;
        private List<CouponUsage> couponUsages;

        public Builder setCouponId(Long couponId) {
            this.couponId = couponId;
            return this;
        }

        public Builder setCouponCode(String couponCode) {
            this.couponCode = couponCode;
            return this;
        }

        public Builder setDiscountPercentage(double discountPercentage) {
            this.discountPercentage = discountPercentage;
            return this;
        }

        public Builder setStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Builder setCouponStatus(CouponStatus couponStatus) {
            this.couponStatus = couponStatus;
            return this;
        }

        public Builder setMinimumPurchase(double minimumPurchase) {
            this.minimumPurchase = minimumPurchase;
            return this;
        }

        public Builder setCouponDescription(String couponDescription) {
            this.couponDescription = couponDescription;
            return this;
        }

        public Builder setCouponUsages(List<CouponUsage> couponUsages) {
            this.couponUsages = couponUsages;
            return this;
        }

        public CouponDto build() {
            return new CouponDto(this);
        }
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
