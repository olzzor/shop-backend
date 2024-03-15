package com.shop.module.cart.dto;

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
    private boolean activateFlag;
    private List<ProductDto> products;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public CartDto(Long id, boolean activateFlag) {
        this.id = id;
        this.activateFlag = activateFlag;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setActivateFlag(boolean activateFlag) {
        this.activateFlag = activateFlag;
    }

    public void setProducts(List<ProductDto> products) {
        this.products = products;
    }
}
