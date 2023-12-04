package com.bridgeshop.module.coupon.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponListResponse {
    private List<CouponDto> coupons;
    private int totalPages;
}