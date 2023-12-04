package com.bridgeshop.module.coupon.repository;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {
    void deleteAllByCoupon(Coupon coupon);
}