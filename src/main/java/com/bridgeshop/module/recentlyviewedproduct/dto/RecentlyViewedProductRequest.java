package com.bridgeshop.module.recentlyviewedproduct.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecentlyViewedProductRequest {
    private Long id;
    private Long userId;
    private Long productId;
    private String viewedAt;
}
