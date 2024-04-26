package com.shop.module.coupon.repository;

import com.shop.module.coupon.dto.CouponListSearchRequest;
import com.shop.module.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CouponRepositoryCustom {

    Page<Coupon> findByCondition(CouponListSearchRequest couponListSearchRequest, Pageable pageable);

    List<Coupon> findApplicableCouponsForCart(Long userId, List<Long> categoryIds, List<Long> productIds);
}