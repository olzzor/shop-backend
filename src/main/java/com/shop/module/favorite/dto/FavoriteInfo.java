package com.shop.module.favorite.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FavoriteInfo {
    private Long id;
    private boolean isFavorite;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public FavoriteInfo(Long id, boolean isFavorite) {
        this.id = id;
        this.isFavorite = isFavorite;
    }
}
