package com.bridgeshop.module.favorite.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteCheckResponse {
    private Long id;
    private boolean isFavorite;
}
