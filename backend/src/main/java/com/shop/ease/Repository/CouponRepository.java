package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>{

    public Coupon findByCouponCode(String couponCode);

    public boolean existsByCouponCode(String couponCode);

    
}
