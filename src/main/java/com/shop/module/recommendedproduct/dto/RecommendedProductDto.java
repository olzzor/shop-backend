package com.shop.module.recommendedproduct.dto;

import com.shop.module.product.dto.ProductDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RecommendedProductDto {
    private Long id;
    private ProductDto product;
    private int displayOrder;

    // Builder pattern constructor
    @Builder
    public RecommendedProductDto(Long id, int displayOrder) {
        this.id = id;
        this.displayOrder = displayOrder;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(ProductDto productDto) {
        this.product = productDto;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
