package com.shop.module.category.dto;

import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.product.dto.ProductDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CategoryDto {
    private Long id;
    private String code;
    private String name;
    private List<ProductDto> products;
    private List<CouponDto> coupons;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CategoryDto(Long id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public void setCoupons(List<CouponDto> coupons) {
        this.coupons = coupons;
    }
}
