package com.shopxy.ecom.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopxy.ecom.repository.CouponRepository;
import com.shopxy.ecom.repository.CouponUsageRepository;
import com.shopxy.ecom.Util.CouponStatus;
import com.shopxy.ecom.dto.CouponDto;
import com.shopxy.ecom.model.Coupon;
import com.shopxy.ecom.model.CouponUsage;
import com.shopxy.ecom.model.User;

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

    public Boolean checkCouponUsage(User user, String code) {
        Coupon coupon=couponRepository.findByCode(code);
        Boolean exist=couponUsageRepository.existsByUserAndCoupon(user, coupon);
        if(exist){
            return true;
        }
        return false;
    }

    public void updateCouponUsage(User user, Coupon coupon) {
        CouponUsage couponUsage=new CouponUsage();
        couponUsage.setCoupon(coupon);
        couponUsage.setUser(user);
        couponUsage.setUsedAt(LocalDateTime.now());
        couponUsage.setStatus(CouponStatus.USED);
        couponUsageRepository.save(couponUsage);
    }
    
    public void deleteCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code);

        if (coupon != null) {
            couponRepository.delete(coupon);
        } 
    }
    
}
