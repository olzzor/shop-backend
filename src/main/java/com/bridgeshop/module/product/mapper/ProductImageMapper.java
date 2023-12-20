package com.bridgeshop.module.product.mapper;

import com.bridgeshop.module.product.dto.ProductImageDto;
import com.bridgeshop.module.product.entity.ProductImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductImageMapper {

    public ProductImageDto mapToDto(ProductImage productImage) {
        return ProductImageDto.builder()
                .fileUrl(productImage.getFileUrl())
                .fileKey(productImage.getFileKey())
                .displayOrder(productImage.getDisplayOrder())
                .build();
    }

    public List<ProductImageDto> mapToDtoList(List<ProductImage> productImageList) {
        return productImageList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}