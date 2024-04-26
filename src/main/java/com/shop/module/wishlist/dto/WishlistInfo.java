package com.shop.module.wishlist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class WishlistInfo {
    private Long id;
    @JsonProperty("isWishlist")
    private boolean isWishlist;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public WishlistInfo(Long id, boolean isWishlist) {
        this.id = id;
        this.isWishlist = isWishlist;
    }
}
