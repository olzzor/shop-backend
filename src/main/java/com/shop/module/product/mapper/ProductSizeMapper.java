package com.shop.module.product.mapper;

import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.entity.ProductSize;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductSizeMapper {

    public ProductSizeDto mapToDto(ProductSize productSize) {
        return ProductSizeDto.builder()
                .id(productSize.getId())
                .size(productSize.getSize())
                .quantity(productSize.getQuantity())
                .build();
    }

    public List<ProductSizeDto> mapToDtoList(List<ProductSize> productSizeList) {
        return productSizeList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}