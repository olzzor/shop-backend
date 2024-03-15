package com.shop.module.product.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductListSearchRequest {
    private String categoryCode;
    private String code;
    private String name;
//    private String detail;
//    private String quantity;
    private String productSize;
    private String price;
    private String discountPer;
    private String status;
    private String startRegDate;
    private String endRegDate;
    private String startModDate;
    private String endModDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ProductListSearchRequest(String categoryCode, String code, String name,
                                    String productSize, String price, String discountPer, String status,
                                    String startRegDate, String endRegDate, String startModDate, String endModDate) {
        this.categoryCode = categoryCode;
        this.code = code;
        this.name = name;
        this.productSize = productSize;
        this.price = price;
        this.discountPer = discountPer;
        this.status = status;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
        this.startModDate = startModDate;
        this.endModDate = endModDate;
    }
}
