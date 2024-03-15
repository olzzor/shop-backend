package com.shop.module.coupon.repository;

import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.entity.CouponUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponUserRepository extends JpaRepository<CouponUser, Long> {
    void deleteAllByCoupon(Coupon coupon);
}