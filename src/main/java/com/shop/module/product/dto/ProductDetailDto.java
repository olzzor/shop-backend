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
    private String sizeGuide;

    // Builder pattern constructor
    @Builder
    public ProductDetailDto(Long id, String description, String sizeGuide) {
        this.id = id;
        this.description = description;
        this.sizeGuide = sizeGuide;
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

    public void setSizeGuide(String sizeGuide) {
        this.sizeGuide = sizeGuide;
    }
}
