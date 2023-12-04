package com.bridgeshop.module.coupon.repository;

import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponProductRepository extends JpaRepository<CouponProduct, Long> {
    void deleteAllByCoupon(Coupon coupon);
}