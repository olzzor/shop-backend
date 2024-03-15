package com.shop.module.coupon.repository;

import com.shop.module.coupon.dto.CouponListSearchRequest;
import com.shop.module.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {

    Page<Coupon> findByCondition(CouponListSearchRequest couponListSearchRequest, Pageable pageable);
}