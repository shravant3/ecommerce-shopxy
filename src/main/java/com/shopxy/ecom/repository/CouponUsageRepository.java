package com.shopxy.ecom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopxy.ecom.model.Coupon;
import com.shopxy.ecom.model.CouponUsage;
import com.shopxy.ecom.model.User;
@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage,Long> {

    public CouponUsage findByUserAndCoupon(User user,Coupon coupon);
    public boolean existsByUserAndCoupon(User user, Coupon coupon);
     
}
