package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Coupon;
import com.shop.ease.model.CouponUsage;
import com.shop.ease.model.MyUserDetails;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage,Long> {

    public CouponUsage findByMyUserDetailsAndCoupon(MyUserDetails user, Coupon coupon);
    public boolean existsByMyUserDetailsAndCoupon(MyUserDetails user, Coupon coupon);

}
