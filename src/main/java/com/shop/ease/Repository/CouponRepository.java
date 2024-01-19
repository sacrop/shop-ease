package com.shop.ease.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shop.ease.model.Coupon;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>{

    public Coupon findByCode(String code);

    public boolean existsByCode(String code);

    
}
