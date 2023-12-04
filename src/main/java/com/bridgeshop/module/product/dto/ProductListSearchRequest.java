package com.bridgeshop.module.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
