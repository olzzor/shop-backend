package com.bridgeshop.module.coupon.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponListAvailableResponse {
    private Long cartProductId;
    private List<CouponDto> coupons;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponListAvailableResponse(Long cartProductId, List<CouponDto> coupons) {
        this.cartProductId = cartProductId;
        this.coupons = coupons;
    }
}
