package com.bridgeshop.module.recentlyviewedproduct.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentlyViewedProductListResponse {
    private List<RecentlyViewedProductDto> recentlyViewedProducts;
    private int totalPages;
}
