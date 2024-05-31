package com.shop.module.product.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductDetailDto {
    private Long id;
    private ProductDto product;
    private String description;
    private String content;

    // Builder pattern constructor
    @Builder
    public ProductDetailDto(Long id, String description, String content) {
        this.id = id;
        this.description = description;
        this.content = content;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
