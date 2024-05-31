package com.shop.module.product.mapper;

import com.shop.module.product.dto.ProductImageDto;
import com.shop.module.product.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    public ProductImageDto mapToDto(ProductImage productImage) {
        return ProductImageDto.builder()
                .id(productImage.getId())
                .fileUrl(productImage.getFileUrl())
                .fileKey(productImage.getFileKey())
                .displayOrder(productImage.getDisplayOrder())
                .build();
    }

    public List<ProductImageDto> mapToDtoList(List<ProductImage> productImageList) {
        return productImageList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}