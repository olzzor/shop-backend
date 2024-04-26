package com.shop.module.cart.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartDto {
    private Long id;
    private UserDto user;
    @JsonProperty("isActivate")
    private boolean isActivate;
    private List<ProductDto> products;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CartDto(Long id, boolean isActivate) {
        this.id = id;
        this.isActivate = isActivate;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
