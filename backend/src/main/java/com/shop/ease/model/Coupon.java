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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Coupon code is required")
    private String code;

    @Min(value = 0, message = "Discount percentage must be greater than or equal to 0")
    @Max(value = 100, message = "Discount percentage must be less than or equal to 100")
    private double discountPercentage;

    private LocalDate startDate;

    private LocalDate endDate;

    @Column(name = "coupon_status")
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Min(value = 0, message = "Minimum purchase must be greater than or equal to 0")
    private double minPurchase;

    private String CouponDescription;

    @OneToMany(mappedBy = "coupon")
    private List<CouponUsage> couponUsages = new ArrayList<>();

    @Override
    public String toString() {
        return "Coupon [id=" + id + ", code=" + code + ", discountPercentage=" + discountPercentage + ", startDate="
                + startDate + ", endDate=" + endDate + ", status=" + status + ", minPurchase=" + minPurchase
                + ", CouponDescription=" + CouponDescription + "]";
    }

   

   
}
