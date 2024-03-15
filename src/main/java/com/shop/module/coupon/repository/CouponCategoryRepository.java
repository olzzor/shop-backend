package com.shop.module.coupon.repository;

import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.entity.CouponCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponCategoryRepository extends JpaRepository<CouponCategory, Long> {
    void deleteAllByCoupon(Coupon coupon);
}