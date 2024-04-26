package com.shop.module.recommendedproduct.mapper;

import com.shop.module.recommendedproduct.dto.RecommendedProductDto;
import com.shop.module.recommendedproduct.entity.RecommendedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecommendedProductMapper {

    public RecommendedProductDto mapToDto(RecommendedProduct recommendedProduct) {
        return RecommendedProductDto.builder()
                .id(recommendedProduct.getId())
                .displayOrder(recommendedProduct.getDisplayOrder())
                .build();
    }

    public List<RecommendedProductDto> mapToDtoList(List<RecommendedProduct> recommendedProductList) {
        return recommendedProductList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}