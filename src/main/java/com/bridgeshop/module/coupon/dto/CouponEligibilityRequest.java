package com.bridgeshop.module.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponEligibilityRequest {
    private Long cartProductId;
    private Long productId;
    private Long categoryId;
}
