package com.shop.ease.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shop.ease.Repository.CouponRepository;
import com.shop.ease.Repository.CouponUsageRepository;
import com.shop.ease.Util.CouponStatus;
import com.shop.ease.dto.CouponDto;
import com.shop.ease.model.Coupon;
import com.shop.ease.model.CouponUsage;
import com.shop.ease.model.UserDtls;

@Service
public class CouponService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired 
    private CouponUsageRepository couponUsageRepository;

    public Coupon convertCouponDto(CouponDto couponDto){
        Coupon coupon=new Coupon();
        coupon=modelMapper.map(couponDto,Coupon.class);
        return coupon;
    }

    public void saveCoupon(Coupon coupon) {
        couponRepository.save(coupon);
    }

    public Coupon findByCouponCode(String code){
       return couponRepository.findByCode(code);
        
    }

    public boolean checkExistCode(String code) {

        return couponRepository.existsByCode(code);
    }

    public List<Coupon> getAllCoupons() {

        return couponRepository.findAll();
    }

    public Boolean checkCouponUsage(UserDtls user, String code) {
        Coupon coupon=couponRepository.findByCode(code);
        Boolean exist=couponUsageRepository.existsByUserAndCoupon(user, coupon);
        if(exist){
            return true;
        }
        return false;
    }

    public void updateCouponUsage(UserDtls user, Coupon coupon) {
        CouponUsage couponUsage=new CouponUsage();
        couponUsage.setCoupon(coupon);
        couponUsage.setUser(user);
        couponUsage.setUsedAt(LocalDateTime.now());
        couponUsage.setStatus(CouponStatus.USED);
        couponUsageRepository.save(couponUsage);
    }

    public Coupon findByCouponId(Long id) {
        Coupon coupon=couponRepository.findById(id).get();
        return coupon;
    }

    public void updateCouponStatus(){
        for(Coupon coup:couponRepository.findAll()){
            LocalDate date=coup.getEndDate();
            LocalDate end=LocalDate.now();
            if(date.isBefore(end)){
                coup.setStatus(CouponStatus.EXPIRED);

            }
            couponRepository.save(coup);
        }
    }
    
    
}
