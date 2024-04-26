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
    private String codeRef;
    private String name;
    private String slug;
    private List<ProductDto> products;
    private List<CouponDto> coupons;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CategoryDto(Long id, String code, String codeRef, String name, String slug) {
        this.id = id;
        this.code = code;
        this.codeRef = codeRef;
        this.name = name;
        this.name = slug;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCodeRef(String codeRef) {
        this.codeRef = codeRef;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }

    public void setCoupons(List<CouponDto> coupons) {
        this.coupons = coupons;
    }
}
