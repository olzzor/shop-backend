package com.shop.module.coupon.dto;

import com.shop.module.coupon.entity.CouponDiscountType;
import com.shop.module.coupon.entity.CouponStatus;
import com.shop.module.coupon.entity.CouponType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CouponUpdateRequest {
    private Long id;
    private CouponType type;
    private String code;
    private String name;
    private String detail;
    private int minAmount;
    private CouponDiscountType discountType;
    private int discountValue;
    private List<String> categories;
    private List<Long> products;
    private List<Long> users;
    private String startValidDate;
    private String endValidDate;
    private CouponStatus status;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponUpdateRequest(Long id, CouponType type, String code, String name,
                               String detail, int minAmount, CouponDiscountType discountType,
                               int discountValue, List<String> categories, List<Long> products,
                               List<Long> users, String startValidDate, String endValidDate,
                               CouponStatus status) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.name = name;
        this.detail = detail;
        this.minAmount = minAmount;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.categories = categories;
        this.products = products;
        this.users = users;
        this.startValidDate = startValidDate;
        this.endValidDate = endValidDate;
        this.status = status;
    }
}

