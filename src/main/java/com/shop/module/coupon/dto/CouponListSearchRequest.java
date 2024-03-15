package com.shop.module.coupon.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CouponListSearchRequest(String id, String type, String code, String name,
                                   String minAmount, String discountType, String discountValue,
                                   String startStartValidDate, String endStartValidDate,
                                   String startEndValidDate, String endEndValidDate, String status) {
        this.id = id;
        this.type = type;
        this.code = code;
        this.name = name;
        this.minAmount = minAmount;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startStartValidDate = startStartValidDate;
        this.endStartValidDate = endStartValidDate;
        this.startEndValidDate = startEndValidDate;
        this.endEndValidDate = endEndValidDate;
        this.status = status;
    }
}
