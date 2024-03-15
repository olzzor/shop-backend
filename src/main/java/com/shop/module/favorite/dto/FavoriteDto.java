package com.shop.module.favorite.dto;

import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteDto {
    private Long id;
    private UserDto user;
    private ProductDto product;
    private ProductSizeDto productSize;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public FavoriteDto(Long id) {
        this.id = id;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setProduct(ProductDto product) {
        this.product = product;
    }

    public void setProductSize(ProductSizeDto productSize) {
        this.productSize = productSize;
    }
}
