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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponDto {

    private Long id;
    

    @NotBlank(message = "Coupon code is required")
    private String code;

    @NotNull(message = "discountPercentage cannot be null")
    @Min(value = 0, message = "Discount percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Discount percentage must be less than or equal to 100")
    private double discountPercentage;

    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @Future(message = "End date must be in the future")
    private LocalDate endDate;

    
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Min(value = 0, message = "Minimum purchase must be greater than or equal to 0")
    private double minPurchase;

    private String CouponDescription;

   
    private List<CouponUsage> couponUsages;
}
