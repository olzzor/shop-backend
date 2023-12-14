package com.bridgeshop.module.coupon.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponListResponse {
    private List<CouponDto> coupons;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponListResponse(List<CouponDto> coupons, int totalPages) {
        this.coupons = coupons;
        this.totalPages = totalPages;
    }
}