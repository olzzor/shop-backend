package com.bridgeshop.module.coupon.dto;

import com.bridgeshop.module.coupon.entity.CouponDiscountType;
import com.bridgeshop.module.coupon.entity.CouponStatus;
import com.bridgeshop.module.coupon.entity.CouponType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CouponUpdateRequest {
    private Long id;
    private CouponType type;
    private String code;
    private String name;
    private String detail;
    private int minAmount;
    private CouponDiscountType discountType;
    private int discountValue;
    private List<Long> categories;
    private List<Long> products;
    private List<Long> users;
    private String startValidDate;
    private String endValidDate;
    private CouponStatus status;
}

