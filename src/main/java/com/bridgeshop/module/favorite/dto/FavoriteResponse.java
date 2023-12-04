package com.bridgeshop.module.favorite.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
    private Long productId;
    private boolean isFavorite;
}
