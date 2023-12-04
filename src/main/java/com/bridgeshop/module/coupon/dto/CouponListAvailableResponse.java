package com.bridgeshop.module.coupon.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponListAvailableResponse {
    private Long cartProductId;
    private List<CouponDto> coupons;
}
