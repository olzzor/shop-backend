package com.bridgeshop.module.recentlyviewedproduct.dto;

import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.user.dto.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RecentlyViewedProductDto {
    private Long id;
    private UserDto user;
    private ProductDto product;
    private LocalDateTime viewedAt;

    // Builder pattern constructor
    @Builder
    public RecentlyViewedProductDto(Long id, LocalDateTime viewedAt) {
        this.id = id;
        this.viewedAt = viewedAt;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto userDto) {
        this.user = userDto;
    }

    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }
}
