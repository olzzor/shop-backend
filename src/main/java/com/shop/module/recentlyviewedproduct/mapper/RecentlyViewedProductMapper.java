package com.shop.module.recentlyviewedproduct.mapper;

import com.shop.module.recentlyviewedproduct.dto.RecentlyViewedProductDto;
import com.shop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecentlyViewedProductMapper {

    public RecentlyViewedProductDto mapToDto(RecentlyViewedProduct recentlyViewedProduct) {
        return RecentlyViewedProductDto.builder()
                .id(recentlyViewedProduct.getId())
                .viewedAt(recentlyViewedProduct.getViewedAt())
                .build();
    }

    public List<RecentlyViewedProductDto> mapToDtoList(List<RecentlyViewedProduct> recentlyViewedProductList) {
        return recentlyViewedProductList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}