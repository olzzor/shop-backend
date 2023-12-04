package com.bridgeshop.module.category.dto;

import com.bridgeshop.module.coupon.dto.CouponDto;
import com.bridgeshop.module.product.dto.ProductDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private String code;
    private String name;
    private List<ProductDto> products;
    private List<CouponDto> coupons;
}
