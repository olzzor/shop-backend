package com.shop.module.coupon.repository;

import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.entity.CouponProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponProductRepository extends JpaRepository<CouponProduct, Long> {
    void deleteAllByCoupon(Coupon coupon);
}