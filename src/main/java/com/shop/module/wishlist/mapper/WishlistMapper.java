package com.shop.module.wishlist.mapper;

import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.wishlist.dto.WishlistDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WishlistMapper {

    public WishlistDto mapToDto(Wishlist wishlist) {
        return WishlistDto.builder()
                .id(wishlist.getId())
                .build();
    }

    public List<WishlistDto> mapToDtoList(List<Wishlist> wishlistList) {
        return wishlistList.stream().map(this::mapToDto).collect(Collectors.toList());
    }
}