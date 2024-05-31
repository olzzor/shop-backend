package com.shop.module.product.mapper;

import com.shop.module.product.dto.ProductDetailDto;
import com.shop.module.product.entity.ProductDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDetailMapper {

    public ProductDetailDto mapToDto(ProductDetail productDetail) {
        return ProductDetailDto.builder()
                .id(productDetail.getId())
                .description(productDetail.getDescription())
                .content(productDetail.getContent())
                .build();
    }
}