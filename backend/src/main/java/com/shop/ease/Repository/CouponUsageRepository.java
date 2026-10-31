package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Coupon;
import com.shop.ease.model.CouponUsage;
import com.shop.ease.model.UserDtls;
@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage,Long> {

    public CouponUsage findByUserAndCoupon(UserDtls user,Coupon coupon);
    public boolean existsByUserAndCoupon(UserDtls user, Coupon coupon);
     
}
