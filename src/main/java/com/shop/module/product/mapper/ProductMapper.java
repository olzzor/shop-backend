package com.shop.module.product.mapper;

import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    public ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .code(product.getCode())
                .name(product.getName())
                .price(product.getPrice())
                .discountPer(product.getDiscountPer())
                .isDisplay(product.isDisplay())
                .status(product.getStatus())
                .regDate(product.getRegDate())
                .modDate(product.getModDate())
                .build();
    }

    public List<ProductDto> mapToDtoList(List<Product> productList) {
        return productList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}