package com.shop.ease.model;

import java.time.LocalDateTime;

import com.shop.ease.Util.CouponStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table
public class CouponUsage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private UserDtls user;

    @ManyToOne
    private Coupon coupon;

    @Column(name = "couponUsage_status")
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    private LocalDateTime usedAt;
}
