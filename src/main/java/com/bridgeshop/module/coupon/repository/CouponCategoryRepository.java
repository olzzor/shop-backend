package com.bridgeshop.module.coupon.repository;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Long> {
    void deleteAllByCoupon(Coupon coupon);
}