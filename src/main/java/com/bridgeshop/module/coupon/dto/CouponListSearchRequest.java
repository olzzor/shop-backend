package com.bridgeshop.module.coupon.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponListSearchRequest {
    private String id;
    private String type;
    private String code;
    private String name;
    private String minAmount;
    private String discountType;
    private String discountValue;
    private String startStartValidDate;
    private String endStartValidDate;
    private String startEndValidDate;
    private String endEndValidDate;
    private String status;
}
