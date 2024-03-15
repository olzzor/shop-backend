package com.shop.module.coupon.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponEligibilityRequest {
    private Long cartProductId;
    private Long productId;
    private Long categoryId;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponEligibilityRequest(Long cartProductId, Long productId, Long categoryId) {
        this.cartProductId = cartProductId;
        this.productId = productId;
        this.categoryId = categoryId;
    }
}
