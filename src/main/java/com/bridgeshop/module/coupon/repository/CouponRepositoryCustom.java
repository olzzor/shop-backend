package com.bridgeshop.module.coupon.repository;

import com.bridgeshop.module.coupon.dto.CouponListSearchRequest;
import com.bridgeshop.module.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponRepositoryCustom {

    Page<Coupon> findByCondition(CouponListSearchRequest couponListSearchRequest, Pageable pageable);
}